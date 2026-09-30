package com.example.Quiz.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "essay_answers", indexes = {
        @Index(name = "idx_essay_answers_question", columnList = "question_id")},
        uniqueConstraints = {
@UniqueConstraint(name = "uq_essay_answer", columnNames = {"assignment_answer_id", "question_id"})
})
@Getter @Setter
@NoArgsConstructor
public class EssayAnswer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_answer_id", nullable = false)
    private AssignmentAnswer assignmentAnswer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id", nullable = false)
    private EssayQuestion question;

    @Column(name = "answer", columnDefinition = "TEXT", nullable = false, length = 3000)
    private String answer;

    @Column(name = "grade")
    private Short grade;

}
