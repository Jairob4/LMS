package edu.unimagdalena.lms.services;

import java.util.List;
import java.util.UUID;

//No se si eliminar esta interface, no se si es necesaria el repo del profe no tiene 
//member profile, pero por ahora la dejo

public interface InstructorProfileService {
    InstructorProfileResponse create(InstructorProfileCreateRequest request);
    InstructorProfileResponse get(UUID id);
    InstructorProfileResponse getByPhone(String phone);
    InstructorProfileResponse update(UUID id, InstructorProfileUpdateRequest request);
    List<InstructorProfileResponse> list();    
    void delete(UUID id);

// Revisa este bro
    InstructorProfileResponse getByInstructorId(UUID instructorId);
}
