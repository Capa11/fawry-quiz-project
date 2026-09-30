package com.example.Quiz.model;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(
        name = "assignment_answers",
        indexes = {
                @Index(name = "idx_points", columnList = "points"),
                @Index(name = "idx_start_date", columnList = "start_date")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_assignment", columnNames = {"user_id", "assignment_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
public class AssignmentAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

    @Column(name = "start_date")
    @Builder.Default
    private LocalDateTime startDate = LocalDateTime.now();

    @Builder.Default
    private Long points = 0L;

    @Column(name = "is_submitted")
    @Builder.Default
    private Boolean isSubmitted = false;


    @OneToMany(mappedBy = "assignmentAnswer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<McqAnswer> mcqAnswers;

    @OneToMany(mappedBy = "assignmentAnswer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TrueFalseAnswer> trueFalseAnswers;

    @OneToMany(mappedBy = "assignmentAnswer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<EssayAnswer> essayAnswers;
}
