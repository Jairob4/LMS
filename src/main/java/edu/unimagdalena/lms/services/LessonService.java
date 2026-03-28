package edu.unimagdalena.lms.services;

import edu.unimagdalena.lms.api.dto.LessonDtos.CreateRequest;
import edu.unimagdalena.lms.api.dto.LessonDtos.Response;
import edu.unimagdalena.lms.api.dto.LessonDtos.UpdateRequest;
import java.util.List;
import java.util.UUID;

public interface LessonService {
    Response create(CreateRequest request);
    Response get(UUID id);
    Response update(UUID id, UpdateRequest request);
    List<Response> list();
    void delete(UUID id);
}
