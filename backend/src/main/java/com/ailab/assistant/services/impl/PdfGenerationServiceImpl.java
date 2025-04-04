package com.ailab.assistant.services.impl;

import com.ailab.assistant.dtos.ExperimentDto;
import com.ailab.assistant.models.LabManual;
import com.ailab.assistant.models.Subject;
import com.ailab.assistant.models.User;
import com.ailab.assistant.repositories.LabManualRepository;
import com.ailab.assistant.repositories.SubjectRepository;
import com.ailab.assistant.services.PdfGenerationService;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PdfGenerationServiceImpl implements PdfGenerationService {

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private LabManualRepository labManualRepository;

    @Override
    public byte[] generateLabManual(long subjectId, User teacher) throws DocumentException {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(()-> new RuntimeException("Subject not found"));
        List<LabManual> labManualList = labManualRepository.findBySubjectAndTeacherAndIsFinal(subject,teacher, true);
        List<ExperimentDto> experimentDtos = new ArrayList<>();
        for (LabManual labManual : labManualList) {
            experimentDtos.add(new ExperimentDto(labManual.getExperimentNo(), labManual.getTitle(),labManual.getContent()));
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();

        // Generate Front Page
        Context frontPageContext = new Context();
        frontPageContext.setVariable("subjectName", subject.getSubjectName());
        frontPageContext.setVariable("subjectCode", subject.getSubjectCode());
        frontPageContext.setVariable("professor", teacher.getUserName());
        frontPageContext.setVariable("department", teacher.getDepartment().getDepartmentName());
//        frontPageContext.setVariable("academicYear", academicYear);
        String frontPageHtml = templateEngine.process("lab-manual-frontpage", frontPageContext);

        // Generate Index Page
        Context indexContext = new Context();
        indexContext.setVariable("experiments", experimentDtos);
        String indexHtml = templateEngine.process("lab-manual-index", indexContext);

        // Generate Experiment Pages
        Context experimentContext = new Context();
        experimentContext.setVariable("experiments", experimentDtos);
        String experimentHtml = templateEngine.process("lab-manual-experiments", experimentContext);

        // Combine all sections into one HTML document
        String fullHtml = "<!DOCTYPE html><html><head>" +
                "<meta charset='UTF-8'/>" +
                "<style>@page { size: A4; margin: 20mm; }</style>" +
                "</head><body>" +
                frontPageHtml +
                indexHtml +
                experimentHtml +
                "</body></html>";

        // Render the complete document
        renderer.setDocumentFromString(fullHtml, "file:///");
        renderer.layout();
        renderer.createPDF(outputStream);

        return outputStream.toByteArray();
    }

    @Override
    public byte[] generateExperimentPdf(Long experimentId) throws DocumentException {

        LabManual experiment = labManualRepository.findById(experimentId)
                .orElseThrow(()-> new RuntimeException("Experiment not found with id: "+ experimentId));

        List<ExperimentDto> experimentDtos = new ArrayList<>();
            experimentDtos.add(new ExperimentDto(experiment.getExperimentNo(), experiment.getTitle(),experiment.getContent()));


        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();

        // Generate Experiment Pages
        Context experimentContext = new Context();
        experimentContext.setVariable("experiments", experimentDtos);
        String experimentHtml = templateEngine.process("lab-manual-experiments", experimentContext);

        // Combine all sections into one HTML document
        String fullHtml = "<!DOCTYPE html><html><head>" +
                "<meta charset='UTF-8'/>" +
                "<style>@page { size: A4; margin: 20mm; }</style>" +
                "</head><body>" +
                experimentHtml +
                "</body></html>";

        // Render the complete document
        renderer.setDocumentFromString(fullHtml, "file:///");
        renderer.layout();
        renderer.createPDF(outputStream);

        return outputStream.toByteArray();
    }
}
