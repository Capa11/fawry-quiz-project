package com.example.Quiz.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "essay_questions",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_essay_question", columnNames = {"assignment_id", "question_number"})
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class EssayQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;

    @Column(name = "question_number")
    private Short questionNumber;

    @Column(name = "question_body", nullable = false, columnDefinition = "TEXT")
    @Size(min = 1, max = 4000)
    private String questionBody;

    @Column(name = "essay_correct_answer", columnDefinition = "TEXT")
    @Size(min = 1, max = 4000)
    private String essayCorrectAnswer;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Column(name = "points")
    @Builder.Default
    private Short points = 1;


    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
