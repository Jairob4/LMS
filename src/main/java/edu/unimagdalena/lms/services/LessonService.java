package edu.unimagdalena.lms.services;

import edu.unimagdalena.lms.api.dto.LessonDtos.CreateRequest;
import edu.unimagdalena.lms.api.dto.LessonDtos.Response;
import edu.unimagdalena.lms.api.dto.LessonDtos.UpdateRequest;
import java.util.List;
import java.util.UUID;

public interface LessonService {
    LessonResponse create(LessonCreateRequest request);
    LessonResponse get(UUID id);
    LessonResponse update(UUID id, LessonUpdateRequest request);
    List<LessonResponse> list();    
    void delete(UUID id);
}
