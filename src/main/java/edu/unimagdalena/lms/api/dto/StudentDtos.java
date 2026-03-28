package edu.unimagdalena.lms.api.dto;

import java.time.Instant;
import java.util.UUID;

public class StudentDtos {
    public record CreateRequest(
	    String email,
	    String fullName
    ) {
    }

    public record UpdateRequest(
	    String email,
	    String fullName
    ) {
    }

    public record Response(
	    UUID id,
	    String email,
	    String fullName,
	    Instant createdAt,
	    Instant updatedAt
    ) {
    }
}
