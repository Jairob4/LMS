package edu.unimagdalena.lms.services;

import edu.unimagdalena.lms.api.dto.CourseDtos.CourseCreateRequest;
import edu.unimagdalena.lms.api.dto.CourseDtos.CourseResponse;
import edu.unimagdalena.lms.api.dto.CourseDtos.CourseUpdateRequest;
import java.util.UUID;
import java.util.List;

public interface CourseService {
    CourseResponse create(CourseCreateRequest request);
    CourseResponse get(UUID id);
    CourseResponse update(UUID id, CourseUpdateRequest request);
    List<CourseResponse> list();
    void activateCourse(UUID id);
    void desactivateCourse(UUID id);
    void delete(UUID id);
    boolean isCourseActive(UUID id);

    //revisa estos metodos, no se si son necesarios
    boolean canStudentEnroll(UUID courseId, UUID studentId);
    boolean belongsToInstructor(UUID courseId, UUID instructorId);
}
