package edu.unimagdalena.lms.services;

import edu.unimagdalena.lms.api.dto.LessonDtos.LessonCreateRequest;
import edu.unimagdalena.lms.api.dto.LessonDtos.LessonResponse;
import edu.unimagdalena.lms.api.dto.LessonDtos.LessonUpdateRequest;
import java.util.List;
import java.util.UUID;

public interface LessonService {
    LessonResponse create(LessonCreateRequest request);
    LessonResponse get(UUID id);
    LessonResponse update(UUID id, LessonUpdateRequest request);
    List<LessonResponse> list();    
    void delete(UUID id);
}
