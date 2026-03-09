package edu.unimagdalena.lms.entities;
import java.util.*;
import lombok.*;
import java.time.Instant;
import jakarta.persistence.*;

@Entity
@Table(name = "assessments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Assesment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private int score;

    @Column(name = "taken_at", nullable = false)
    private Instant takenAt;

}
