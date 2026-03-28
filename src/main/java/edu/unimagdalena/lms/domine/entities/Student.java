package edu.unimagdalena.lms.domine.entities;

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
    @Builder.Default
    private Set<Enrollment> enrollments = new HashSet<>();

    public void addEnrollment(Enrollment enrollment){
        enrollments.add(enrollment);
        enrollment.setStudent(this);
    }

    @OneToMany(mappedBy = "student")
    @Builder.Default
    private Set<Assesment> assesments = new HashSet<>();

    public void addAssessment(Assesment assessment){
        assesments.add(assessment);
        assessment.setStudent(this);
    }   

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

}
