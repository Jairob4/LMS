package edu.unimagdalena.lms.services;

import java.util.UUID;

import edu.unimagdalena.lms.api.dto.InstructorProfileDtos.InstructorProfileCreateRequest;
import edu.unimagdalena.lms.api.dto.InstructorProfileDtos.InstructorProfileResponse;
import java.util.Optional;  
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface InstructorProfileService {
    InstructorProfileResponse createInstructorProfile(InstructorProfileCreateRequest request, UUID instructorId);
    InstructorProfileResponse getInstructorProfile(UUID id);
    void delete(UUID id);
    Page<InstructorProfileResponse> findByInstructorFullNameIgnoreCase(String fullName, Pageable pageable);
    Optional<InstructorProfileResponse> findByPhoneNumber(String phone);
    boolean existsByPhoneNumber(String phone);
}
