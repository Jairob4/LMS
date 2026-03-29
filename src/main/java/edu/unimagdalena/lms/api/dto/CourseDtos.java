package edu.unimagdalena.lms.api.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class CourseDtos {
    public record CourseCreateRequest(
	    UUID instructorId,
	    String title,
	    String status,
	    boolean active
    ) implements Serializable { }

    public record CourseUpdateRequest(
	    String title,
	    String status,
	    Boolean active
    ) implements Serializable {}

    public record CourseResponse(
	    UUID id,
	    UUID instructorId,
	    String title,
	    String status,
	    boolean active,
	    Instant createdAt,
	    Instant updatedAt
    ) implements Serializable { }
}
