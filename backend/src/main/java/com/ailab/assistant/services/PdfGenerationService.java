package com.ailab.assistant.services;

import com.ailab.assistant.models.User;
import com.lowagie.text.DocumentException;

public interface PdfGenerationService {

    byte[] generateLabManual(long subjectId, User teacher) throws DocumentException;

    byte[] generateExperimentPdf(Long experimentId) throws DocumentException;
}
