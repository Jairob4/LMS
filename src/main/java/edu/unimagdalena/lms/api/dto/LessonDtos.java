package edu.unimagdalena.lms.api.dto;

import java.io.Serializable;
import java.util.UUID;

public class LessonDtos {
    public record LessonCreateRequest(
	    UUID courseId,
	    String title,
	    int orderIndex
    ) implements Serializable { }

    public record LessonUpdateRequest(
	    String title,
	    Integer orderIndex
    ) implements Serializable { }

    public record LessonResponse(
	    UUID id,
	    UUID courseId,
	    String title,
	    int orderIndex
    ) implements Serializable { }
}
