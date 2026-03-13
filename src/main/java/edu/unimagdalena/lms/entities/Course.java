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
    @Builder.Default
    private Set<Lesson> lessons = new HashSet<>();

    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
        lesson.setCourse(this);
    }

    public void removeLesson(Lesson lesson) {
        lessons.remove(lesson);
        lesson.setCourse(null);
    }   
    
    @OneToMany(mappedBy = "course")
    @Builder.Default
    private Set<Assesment> assesments = new HashSet<>();

    public void addAssessment(Assesment assessment){
        assesments.add(assessment);
        assessment.setCourse(this);
    }

     public void removeAssessment(Assesment assessment){
        assesments.remove(assessment);
        assessment.setCourse(null);
    }

    @OneToMany(mappedBy = "course")
    private Set<Enrollment> enrollments;

    public void addEnrollment(Enrollment enrollment) {
        if (enrollments == null) {
            enrollments = new HashSet<>();
        }
        enrollments.add(enrollment);
    }

    @OneToMany(mappedBy = "course")
    private Set<Assesment> assesments;

    public void addAssesment(Assesment assesment) {
        if (assesments == null) {
            assesments = new HashSet<>();
        }
        assesments.add(assesment);
    }

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

}
