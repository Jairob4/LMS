package edu.unimagdalena.lms.services;

import edu.unimagdalena.lms.api.dto.AssessmentDtos.AssessmentCreateRequest;
import edu.unimagdalena.lms.api.dto.AssessmentDtos.AssessmentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.UUID;

public interface AssessmentService {
    AssessmentResponse createAssessment(AssessmentCreateRequest request, UUID studentId, UUID courseId);
    AssessmentResponse getAssessment(UUID id);
    void delete(UUID id);
    Page<AssessmentResponse> findByStudentFullName(String fullname, Pageable pageable);
    Page<AssessmentResponse> findByCourseId(UUID courseId, Pageable pageable);
    Page<AssessmentResponse> findByType(String type, Pageable pageable);
    Page<AssessmentResponse> findByStudentIdAndScoreGreaterThanEqual(UUID studentId, int score, Pageable pageable);
    Page<AssessmentResponse> findByStudentIdAndType(UUID studentId, String type, Pageable pageable);
    Page<AssessmentResponse> findByTakenAtBetween(Instant startDate, Instant endDate, Pageable pageable);

}
