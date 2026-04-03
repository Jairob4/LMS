package edu.unimagdalena.lms.api.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class EnrollmentDtos {
    public record EnrollmentCreateRequest(
	    UUID studentId,
	    UUID courseId,
	    String status,
	    Instant enrolledAt
    ) implements Serializable { }

    public record EnrollmentUpdateRequest(
	    String status,
	    Instant enrolledAt
    ) implements Serializable { }

    public record EnrollmentResponse(
	    UUID id,
	    UUID studentId,
	    UUID courseId,
	    String status,
	    Instant enrolledAt
    ) implements Serializable { }

    public record CourseEnrollmentStatsResponse(
	    UUID courseId,
	    String courseTitle,
	    long totalEnrollments
    ) implements Serializable { }
}
