package edu.unimagdalena.lms.services;

import edu.unimagdalena.lms.api.dto.EnrollmentDtos.CourseEnrollmentStatsResponse;
import edu.unimagdalena.lms.api.dto.EnrollmentDtos.EnrollmentCreateRequest;
import edu.unimagdalena.lms.api.dto.EnrollmentDtos.EnrollmentResponse;
import edu.unimagdalena.lms.api.dto.EnrollmentDtos.EnrollmentUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.UUID;

public interface EnrollmentService {
    EnrollmentResponse createEnrollment(EnrollmentCreateRequest request, UUID studentId, UUID courseId);
    EnrollmentResponse getEnrollment(UUID id);
    EnrollmentResponse updateEnrollment(UUID id, EnrollmentUpdateRequest request);
    void delete(UUID id);
    Page<EnrollmentResponse> findByEnrolledAtAfter(Instant date, Pageable pageable);
    Page<EnrollmentResponse> findByStudentId(UUID studentId, Pageable pageable);
    Page<EnrollmentResponse> findByCourseId(UUID courseId, Pageable pageable);
    Page<EnrollmentResponse> findByStatus(String status, Pageable pageable);
    Page<EnrollmentResponse> findByStudentFullName(String fullName, Pageable pageable);
    Page<EnrollmentResponse> findByCourseTitle(String title, Pageable pageable);
    Page<EnrollmentResponse> findByEnrolledAtBetween(Instant start, Instant end, Pageable pageable);
    Page<CourseEnrollmentStatsResponse> countEnrollmentsByCourse(Pageable pageable);

}
