package edu.unimagdalena.lms.domine.entities;

import java.util.*;
import lombok.*;
import java.time.Instant;
import jakarta.persistence.*;

@Entity
@Table(name = "instructors")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Instructor {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @OneToMany(mappedBy = "instructor")
    @Builder.Default
    private Set<Course> courses = new HashSet<>();

    public void addCourse(Course course){
        courses.add(course);
        course.setInstructor(this);
    }
    
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

}
