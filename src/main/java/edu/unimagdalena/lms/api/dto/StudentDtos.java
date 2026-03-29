package edu.unimagdalena.lms.api.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

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
	    Instant createdAt,
	    Instant updatedAt
    ) implements Serializable { }
}
