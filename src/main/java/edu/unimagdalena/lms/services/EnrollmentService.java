package edu.unimagdalena.lms.services;

import edu.unimagdalena.lms.api.dto.EnrollmentDtos.CreateRequest;
import edu.unimagdalena.lms.api.dto.EnrollmentDtos.Response;
import edu.unimagdalena.lms.api.dto.EnrollmentDtos.UpdateRequest;
import java.util.List;
import java.util.UUID;

public interface EnrollmentService {
    EnrollmentResponse create(EnrollmentCreateRequest request);
    EnrollmentResponse get(UUID id);
    EnrollmentResponse update(UUID id, EnrollmentUpdateRequest request);
    List<EnrollmentResponse> list();    
    void delete(UUID id);   

}
