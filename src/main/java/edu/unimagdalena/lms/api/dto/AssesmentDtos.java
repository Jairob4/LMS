package edu.unimagdalena.lms.api.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class AssesmentDtos {
    public record AssesmentCreateRequest(
	    UUID studentId,
	    UUID courseId,
	    String type,
	    int score,
	    Instant takenAt
    ) implements Serializable { }

	public record AssesmentUpdateRequest(
		String type,
		Integer score,
		Instant takenAt
	) implements Serializable { }

    public record AssesmentResponse(
	    UUID id,
	    UUID studentId,
	    UUID courseId,
	    String type,
	    int score,
	    Instant takenAt
    ) implements Serializable { }
}
