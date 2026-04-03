package edu.unimagdalena.lms.api.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import edu.unimagdalena.lms.api.dto.AssessmentDtos.AssessmentResponse;
import edu.unimagdalena.lms.api.dto.EnrollmentDtos.EnrollmentResponse;
import edu.unimagdalena.lms.api.dto.LessonDtos.LessonResponse;

public class CourseDtos {
    public record CourseCreateRequest(
	    String title,
	    String status,
	    boolean active,
		Instant createdAt,
		Instant updatedAt
    ) implements Serializable { }

    public record CourseUpdateRequest(
	    String title,
	    String status,
	    Boolean active
    ) implements Serializable {}

    public record CourseResponse(
	    UUID id,
	    String title,
	    String status,
	    boolean active,
	    Instant createdAt,
	    Instant updatedAt,
	    UUID instructorId,
	    Set<AssessmentResponse> assessments,
	    Set<EnrollmentResponse> enrollments,
	    Set<LessonResponse> lessons
    ) implements Serializable { }
}
