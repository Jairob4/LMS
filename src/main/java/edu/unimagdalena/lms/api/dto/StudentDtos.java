package edu.unimagdalena.lms.api.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import edu.unimagdalena.lms.api.dto.EnrollmentDtos.EnrollmentResponse;
import edu.unimagdalena.lms.api.dto.AssessmentDtos.AssessmentResponse;

public class StudentDtos {
    public record StudentCreateRequest(
	    String email,
	    String fullName
    ) implements Serializable { }

    public record StudentUpdateRequest(
	    String email,
	    String fullName
    ) implements Serializable { }

    public record StudentResponse(
	    UUID id,
	    String email,
	    String fullName,
		Set<AssessmentResponse> assessments,
		Set<EnrollmentResponse> enrollments,
	    Instant createdAt,
	    Instant updatedAt
    ) implements Serializable { }
}
