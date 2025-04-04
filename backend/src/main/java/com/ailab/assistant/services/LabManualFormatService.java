package com.ailab.assistant.services;

import com.ailab.assistant.dtos.LabFormatResponse;
import com.ailab.assistant.dtos.LabManualFormatListDto;

import java.util.List;
import java.util.Map;

public interface LabManualFormatService {
    LabFormatResponse findLabFormatBySubjectId(long subjectId);

    List<LabManualFormatListDto> getAllLabManualFormats();
}
