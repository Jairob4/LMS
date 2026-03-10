package edu.unimagdalena.lms.entities;

import java.util.*;
import lombok.*;
import java.time.Instant;
import jakarta.persistence.*;

@Entity
@Table(name = "students")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @OneToMany(mappedBy = "student")
    private Set<Enrollment> enrollments;

    public void addEnrollment(Enrollment enrollment){
        if (enrollments == null) {
            enrollments = new HashSet<>();
        }
        enrollments.add(enrollment);
    }

    @OneToMany(mappedBy = "student")
    private Set<Assesment> assesments;

    public void addAssessment(Assesment assessment){
        if (assesments == null) {
            assesments = new HashSet<>();
        }
        assesments.add(assessment);
    }   

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

}
