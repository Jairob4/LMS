package edu.unimagdalena.lms.services;

import edu.unimagdalena.lms.api.dto.StudentDtos.StudentCreateRequest;
import edu.unimagdalena.lms.api.dto.StudentDtos.StudentResponse;
import edu.unimagdalena.lms.api.dto.StudentDtos.StudentUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;    
import java.util.UUID;

public interface StudentService {
    StudentResponse create(StudentCreateRequest request);
    StudentResponse get(UUID id);
    StudentResponse getByEmail(String email);
    Page<StudentResponse> list(Pageable pageable);
    StudentResponse update(UUID id, StudentUpdateRequest request);
    void delete(UUID id);
}
