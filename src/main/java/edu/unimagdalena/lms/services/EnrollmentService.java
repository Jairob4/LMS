package edu.unimagdalena.lms.services;

import edu.unimagdalena.lms.api.dto.EnrollmentDtos.CreateRequest;
import edu.unimagdalena.lms.api.dto.EnrollmentDtos.Response;
import edu.unimagdalena.lms.api.dto.EnrollmentDtos.UpdateRequest;
import java.util.List;
import java.util.UUID;

public interface EnrollmentService {
    Response create(CreateRequest request);
    Response get(UUID id);
    Response update(UUID id, UpdateRequest request);
    List<Response> list();
    void delete(UUID id);   

}
