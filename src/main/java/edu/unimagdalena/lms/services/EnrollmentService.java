package edu.unimagdalena.lms.services;

import edu.unimagdalena.lms.api.dto.EnrollmentDtos.EnrollmentCreateRequest;
import edu.unimagdalena.lms.api.dto.EnrollmentDtos.EnrollmentResponse;
import edu.unimagdalena.lms.api.dto.EnrollmentDtos.EnrollmentUpdateRequest;
import java.util.List;
import java.util.UUID;

public interface EnrollmentService {
    EnrollmentResponse create(EnrollmentCreateRequest request);
    EnrollmentResponse get(UUID id);
    EnrollmentResponse update(UUID id, EnrollmentUpdateRequest request);
    List<EnrollmentResponse> list();    
    void delete(UUID id);   

}
