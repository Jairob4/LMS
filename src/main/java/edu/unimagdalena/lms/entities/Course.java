package edu.unimagdalena.lms.entities;
import java.util.*;
import lombok.*;
import java.time.Instant;
import jakarta.persistence.*;

@Entity
@Table(name = "courses")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "instructor_id", nullable = false)
    private Instructor instructor;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private boolean active;

    @OneToMany(mappedBy = "course")
    private Set<Lesson> lessons;

    public void addLesson(Lesson lesson) {
        if (lessons == null) {
            lessons = new HashSet<>();
        }
        lessons.add(lesson);
    }

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

}
