package edu.unimagdalena.lms.services;

import edu.unimagdalena.lms.api.dto.CourseDtos.CreateRequest;
import edu.unimagdalena.lms.api.dto.CourseDtos.Response;
import edu.unimagdalena.lms.api.dto.CourseDtos.UpdateRequest;
import java.util.List;
import java.util.UUID;

public interface CourseService {
    Response create(CreateRequest request);
    Response get(UUID id);
    Response update(UUID id, UpdateRequest request);
    List<Response> list();
    void activateCourse(UUID id);
    void desactivateCourse(UUID id);
    void delete(UUID id);
    boolean isCourseActive(UUID id);

    //revisa estos metodos, no se si son necesarios
    boolean canStudentEnroll(UUID courseId, UUID studentId);
    boolean belongsToInstructor(UUID courseId, UUID instructorId);
}
