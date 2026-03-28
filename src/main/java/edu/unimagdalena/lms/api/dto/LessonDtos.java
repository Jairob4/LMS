package edu.unimagdalena.lms.api.dto;

import java.util.UUID;

public class LessonDtos {
    public record CreateRequest(
	    UUID courseId,
	    String title,
	    int orderIndex
    ) {
    }

    public record UpdateRequest(
	    String title,
	    Integer orderIndex
    ) {
    }

    public record Response(
	    UUID id,
	    UUID courseId,
	    String title,
	    int orderIndex
    ) {
    }
}
