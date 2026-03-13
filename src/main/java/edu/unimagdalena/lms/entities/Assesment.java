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
    @Builder.Default
    private Set<Student> student = new HashSet<>();

    public void addStudent(Student s){
        student.add(s);
        s.getAssesments().add(this);

    }

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    @Builder.Default
    private Set<Course> course = new HashSet<>();

     public void addCourse(Course c){
        course.add(c);
        c.getAssesments().add(this);

    }

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private int score;

    @Column(name = "taken_at", nullable = false)
    private Instant takenAt;

}
