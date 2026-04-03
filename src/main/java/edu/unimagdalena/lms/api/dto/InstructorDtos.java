package edu.unimagdalena.lms.api.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import jakarta.validation.constraints.Size;

public class InstructorDtos {
    public record InstructorCreateRequest(
	 @Size(max=320)   String email,
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
	    Instant updatedAt,
		Set<CourseDtos.CourseResponse> courses
    ) implements Serializable { }
}
