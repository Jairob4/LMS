package edu.unimagdalena.lms.services;

import edu.unimagdalena.lms.api.dto.AssesmentDtos.AssesmentCreateRequest;
import edu.unimagdalena.lms.api.dto.AssesmentDtos.AssesmentResponse;
import java.util.List;
import java.util.UUID;

public interface AssesmentService {
    AssesmentResponse create(AssesmentCreateRequest request);
    AssesmentResponse get(UUID id);
    List<AssesmentResponse> list();
    void delete(UUID id);

}
