package edu.unimagdalena.lms.domine.entities;

import java.util.*;
import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "instructor_profiles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class InstructorProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "instructor_id", nullable = false)
    private Instructor instructor;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String bio;

}
