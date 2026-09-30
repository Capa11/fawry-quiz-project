package com.example.Quiz.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "trueFalse_questions",
        indexes = {
                @Index(name = "idx_trueFalse_questions_assignment", columnList = "assignment_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_true_false_question", columnNames = {"assignment_id", "question_number"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class TrueFalseQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;

    @Column(name = "question_body", nullable = false, columnDefinition = "TEXT")
    private String questionBody;

    @Column(nullable = false)
    private boolean answer;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Column(nullable = false)
    private short points = 1;

    @Column(name = "question_number", nullable = false)
    private Short questionNumber;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
