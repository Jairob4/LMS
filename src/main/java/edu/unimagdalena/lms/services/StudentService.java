package edu.unimagdalena.lms.services;

import edu.unimagdalena.lms.api.dto.StudentDtos.CreateRequest;
import edu.unimagdalena.lms.api.dto.StudentDtos.Response;
import edu.unimagdalena.lms.api.dto.StudentDtos.UpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface StudentService {
    Response create(CreateRequest request);
    Response get(UUID id);
    Response getByEmail(String email);
    Page<Response> list(Pageable pageable);
    Response update(UUID id, UpdateRequest request);
    void delete(UUID id);
}
