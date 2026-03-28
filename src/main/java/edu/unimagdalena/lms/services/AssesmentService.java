package edu.unimagdalena.lms.services;

import edu.unimagdalena.lms.api.dto.AssesmentDtos.CreateRequest;
import edu.unimagdalena.lms.api.dto.AssesmentDtos.Response;
import java.util.List;
import java.util.UUID;

public interface AssesmentService {
    AssesmentResponse create(AssesmentCreateRequest request);
    AssesmentResponse get(UUID id);
    List<AssesmentResponse> list();
    void delete(UUID id);

}
