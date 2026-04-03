package edu.unimagdalena.lms.services;

import edu.unimagdalena.lms.api.dto.CourseDtos.CourseCreateRequest;
import edu.unimagdalena.lms.api.dto.CourseDtos.CourseResponse;
import edu.unimagdalena.lms.api.dto.CourseDtos.CourseUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CourseService {
    CourseResponse createCourse(CourseCreateRequest request, UUID instructorId);
    CourseResponse getCourse(UUID id);
    CourseResponse updateCourse(UUID id, CourseUpdateRequest request);
    Page<CourseResponse> list(Pageable pageable);
    Page<CourseResponse> findByInstructorId(UUID instructorId, Pageable pageable);
    Page<CourseResponse> findByStatus(String status, Pageable pageable);
    Page<CourseResponse> findByActive(boolean active, Pageable pageable);
    Page<CourseResponse> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);
    Page<CourseResponse> findByInstructorIdAndActive(UUID instructorId, boolean active, Pageable pageable);
    Page<CourseResponse> findActiveCoursesByInstructor(UUID instructorId, Pageable pageable);
    Page<CourseResponse> findCoursesWithLessons(Pageable pageable);
    Page<CourseResponse> findCoursesWithActiveEnrollments(Pageable pageable);
    Page<CourseResponse> findCoursesWithAssessmentsAboveScore(double score, Pageable pageable);
    void activateCourse(UUID id);
    void desactivateCourse(UUID id);
    void delete(UUID id);
    boolean isCourseActive(UUID id);

}
