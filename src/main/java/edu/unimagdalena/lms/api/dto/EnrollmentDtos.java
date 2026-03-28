package edu.unimagdalena.lms.api.dto;

import java.time.Instant;
import java.util.UUID;

public class EnrollmentDtos {
    public record CreateRequest(
	    UUID studentId,
	    UUID courseId,
	    String status,
	    Instant enrolledAt
    ) {
    }

    public record UpdateRequest(
	    String status,
	    Instant enrolledAt
    ) {
    }

    public record Response(
	    UUID id,
	    UUID studentId,
	    UUID courseId,
	    String status,
	    Instant enrolledAt
    ) {
    }
}
