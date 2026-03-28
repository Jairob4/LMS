package edu.unimagdalena.lms.services;

import edu.unimagdalena.lms.api.dto.InstructorDtos.CreateRequest;
import edu.unimagdalena.lms.api.dto.InstructorDtos.Response;
import edu.unimagdalena.lms.api.dto.InstructorDtos.UpdateRequest;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InstructorService {
    Response create(CreateRequest request);
    Response get(UUID id);
    Response getByEmail(String email);
    Page<Response> list(Pageable pageable);
    Response update(UUID id, UpdateRequest request);
    void delete(UUID id);
    
}
