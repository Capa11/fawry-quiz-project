package com.example.Quiz.model;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Entity
@Table(
        name = "mcq_answers",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_assignment_question",
                        columnNames = {"assignment_answer_id", "question_number"}
                ),
                @UniqueConstraint(
                        name = "uk_assignment_question_choice",
                        columnNames = {"assignment_answer_id", "question_number", "choice_number"}
                )
        },
        indexes = {
                @Index(name = "idx_assignment_answer", columnList = "assignment_answer_id")
        }
)


@Getter @Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class McqAnswer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_answer_id", nullable = false)
    private AssignmentAnswer assignmentAnswer;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "mcq_question_id", nullable = false)
    private McqQuestion mcqQuestion;

    @Column(name = "choice_number", nullable = false)
    private Short choiceNumber;

}
