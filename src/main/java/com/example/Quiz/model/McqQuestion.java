package com.example.Quiz.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "mcq_questions", indexes = {
        @Index(name = "idx_mcq_questions_assignment", columnList = "assignment_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class McqQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;

    @Column(name = "question_number", nullable = false)
    private Short questionNumber;

    @Column(name = "question_body", nullable = false, columnDefinition = "TEXT")
    @Size(min = 1, max = 4000)
    private String questionBody;

    @Column(name = "multiple_answer", nullable = false)
    private boolean multipleAnswer;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Column(nullable = false)
    @Builder.Default
    private short points = 1;

    @OneToMany(mappedBy = "mcqQuestion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<McqChoice> mcqChoices;


    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
