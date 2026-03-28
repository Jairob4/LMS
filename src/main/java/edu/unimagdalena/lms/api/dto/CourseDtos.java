package edu.unimagdalena.lms.api.dto;

import java.time.Instant;
import java.util.UUID;

public class CourseDtos {

    private CourseDtos() {
    }

    public record CreateRequest(
	    UUID instructorId,
	    String title,
	    String status,
	    boolean active
    ) {
    }

    public record UpdateRequest(
	    String title,
	    String status,
	    Boolean active
    ) {
    }

    public record Response(
	    UUID id,
	    UUID instructorId,
	    String title,
	    String status,
	    boolean active,
	    Instant createdAt,
	    Instant updatedAt
    ) {
    }
}
