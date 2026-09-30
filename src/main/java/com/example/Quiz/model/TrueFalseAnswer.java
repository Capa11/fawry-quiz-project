package com.example.Quiz.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "true_false_answers", indexes = {
        @Index(name = "idx_true_false_answers_question", columnList = "question_id")},
                uniqueConstraints = {
                        @UniqueConstraint(name = "uq_true_false_answer", columnNames = {"assignment_answer_id", "question_id"})
                }
)
@Getter @Setter
@NoArgsConstructor
public class TrueFalseAnswer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_answer_id", nullable = false)
    private AssignmentAnswer assignmentAnswer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private TrueFalseQuestion question;


    @Column(nullable = false)
    private boolean answer;

    @Column(name = "is_added_to_note_package")
    private boolean isAddedToNotePackage;

}
