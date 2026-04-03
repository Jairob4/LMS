package edu.unimagdalena.lms.services;

import edu.unimagdalena.lms.api.dto.StudentDtos.StudentCreateRequest;
import edu.unimagdalena.lms.api.dto.StudentDtos.StudentResponse;
import edu.unimagdalena.lms.api.dto.StudentDtos.StudentUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface StudentService {
    StudentResponse createStudent(StudentCreateRequest request);
    StudentResponse getStudent(UUID id);
    StudentResponse getStudentByEmail(String email);
    Page<StudentResponse> list(Pageable pageable);
    Page<StudentResponse> findByFullNameContaining(String name, Pageable pageable);
    Page<StudentResponse> findStudentsWithEnrollmentStatus(String status, Pageable pageable);
    Page<StudentResponse> findStudentsWithAssessments(Pageable pageable);
    StudentResponse updateStudent(UUID id, StudentUpdateRequest request);
    void delete(UUID id);
}
