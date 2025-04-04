package com.ailab.assistant.services.impl;

import com.ailab.assistant.dtos.LabFormatResponse;
import com.ailab.assistant.dtos.LabManualFormatListDto;
import com.ailab.assistant.models.LabManualFormat;
import com.ailab.assistant.models.Subject;
import com.ailab.assistant.repositories.LabManualFormatRepository;
import com.ailab.assistant.repositories.SubjectRepository;
import com.ailab.assistant.services.LabManualFormatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class LabManualFormatServiceImpl implements LabManualFormatService {

    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    private LabManualFormatRepository labManualFormatRepository;

    @Override
    public LabFormatResponse findLabFormatBySubjectId(long subjectId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(()-> new RuntimeException("Subject not found"));

        return new LabFormatResponse(subject.getSubjectName(),subject.getLabManualFormat().getFormatCategory(),subject.getLabManualFormat().getManualFormat());
    }

    @Override
    public List<LabManualFormatListDto> getAllLabManualFormats() {
        List<LabManualFormat> formats = labManualFormatRepository.findAll();
        List<LabManualFormatListDto> formatListDtos = new ArrayList<>();
        for (LabManualFormat format : formats) {
            formatListDtos.add(new LabManualFormatListDto(format.getId(), format.getFormatCategory()));
        }
        return formatListDtos;
    }
}
