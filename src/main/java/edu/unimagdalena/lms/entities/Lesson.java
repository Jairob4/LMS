package edu.unimagdalena.lms.entities;

import java.util.*;
import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "lessons")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false)
    private String title;

    @Column(name = "order_index", nullable = false)
    private int orderIndex;
}
