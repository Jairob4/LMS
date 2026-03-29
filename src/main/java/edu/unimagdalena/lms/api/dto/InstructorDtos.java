package edu.unimagdalena.lms.api.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class InstructorDtos {
    public record InstructorCreateRequest(
	    String email,
	    String fullName
    ) implements Serializable { }

    public record InstructorUpdateRequest(
	    String email,
	    String fullName
    ) implements Serializable { }

    public record InstructorResponse(
	    UUID id,
	    String email,
	    String fullName,
	    Instant createdAt,
	    Instant updatedAt
    ) implements Serializable { }
}
