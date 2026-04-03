package edu.unimagdalena.lms.services;

import edu.unimagdalena.lms.api.dto.LessonDtos.LessonCreateRequest;
import edu.unimagdalena.lms.api.dto.LessonDtos.LessonResponse;
import edu.unimagdalena.lms.api.dto.LessonDtos.LessonUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface LessonService {
    LessonResponse createLesson(LessonCreateRequest request, UUID courseId);
    LessonResponse getLesson(UUID id);
    LessonResponse updateLesson(UUID id, LessonUpdateRequest request);
    Page<LessonResponse> list(Pageable pageable);
    Page<LessonResponse> findByCourseId(UUID courseId, Pageable pageable);
    Page<LessonResponse> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);
    Page<LessonResponse> findByCourseIdOrderByIndex(UUID courseId, Pageable pageable);
    Page<LessonResponse> findLessonsWithCourse(Pageable pageable);
    void delete(UUID id);
}
