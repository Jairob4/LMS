package edu.unimagdalena.lms.services;

import edu.unimagdalena.lms.api.dto.AssesmentDtos.CreateRequest;
import edu.unimagdalena.lms.api.dto.AssesmentDtos.Response;
import java.util.List;
import java.util.UUID;

public interface AssesmentService {
    Response create(CreateRequest request);
    Response get(UUID id);
    List<Response> list();
    void delete(UUID id);

}
