package edu.unimagdalena.lms.api.dto;

    import java.io.Serializable;
    import java.util.UUID;

    public class InstructorProfileDtos {
        public record InstructorProfileCreateRequest(
            String phoneNumber, 
            String bio
        ) implements Serializable{};
        public record InstructorProfileUpdateRequest(
            String phoneNumber, 
            String bio
        ) implements Serializable{};
        public record InstructorProfileResponse(
            UUID id, 
            String phoneNumber, 
            String bio, 
            UUID instructorId
        ) implements Serializable{};
    }
