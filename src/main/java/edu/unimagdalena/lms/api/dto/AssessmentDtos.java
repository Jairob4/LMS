package edu.unimagdalena.lms.api.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class AssessmentDtos {
    public record AssessmentCreateRequest(
	    String type,
	    int score,
	    Instant takenAt
    ) implements Serializable { }

	public record AssessmentUpdateRequest(
		String type,
		Integer score,
		Instant takenAt
	) implements Serializable { }

    public record AssessmentResponse(
	    UUID id,
	    UUID studentId,
	    UUID courseId,
	    String type,
	    int score,
	    Instant takenAt
    ) implements Serializable { }
}
