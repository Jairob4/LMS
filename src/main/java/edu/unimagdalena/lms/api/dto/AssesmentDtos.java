package edu.unimagdalena.lms.api.dto;

import java.time.Instant;
import java.util.UUID;

public class AssesmentDtos {
    public record CreateRequest(
	    UUID studentId,
	    UUID courseId,
	    String type,
	    int score,
	    Instant takenAt
    ) {
    }

    public record UpdateRequest(
	    String type,
	    Integer score,
	    Instant takenAt
    ) {
    }

    public record Response(
	    UUID id,
	    UUID studentId,
	    UUID courseId,
	    String type,
	    int score,
	    Instant takenAt
    ) {
    }
}
