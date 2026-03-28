package edu.unimagdalena.lms.services;

import java.util.UUID;

public interface StudentService {
    StudentResponse create(StudentCreateRequest request);
    StudentResponse get(UUID id);
    StudentResponse getByEmail(String email);
    Page<StudentResponse> list(Pageable pageable);
    StudentResponse update(UUID id, StudentUpdateRequest request);
    void delete(UUID id);
}
