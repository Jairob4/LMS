package edu.unimagdalena.lms.services;

import edu.unimagdalena.lms.api.dto.InstructorDtos.InstructorCreateRequest;
import edu.unimagdalena.lms.api.dto.InstructorDtos.InstructorResponse;
import edu.unimagdalena.lms.api.dto.InstructorDtos.InstructorUpdateRequest;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InstructorService {
    InstructorResponse create(InstructorCreateRequest request);
    InstructorResponse get(UUID id);
    InstructorResponse getByEmail(String email);
    Page<InstructorResponse> list(Pageable pageable);
    InstructorResponse update(UUID id, InstructorUpdateRequest request);
    void delete(UUID id);
    
}
