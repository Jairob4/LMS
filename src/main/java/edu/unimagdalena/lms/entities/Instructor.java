package edu.unimagdalena.lms.entities;

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
    private Set<Course> courses;

    public void addCourse(Course course){
        if (courses == null) {
            courses = new HashSet<>();
        }
        courses.add(course);
    }
    
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

}
