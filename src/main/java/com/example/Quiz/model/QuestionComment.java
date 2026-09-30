package com.example.Quiz.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Table(
        name = "question_comment",
        indexes = {
                @Index(name = "idx_question_comment_created_at", columnList = "created_at")
        }
)
@Entity
@Getter @Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class QuestionComment {

    @Id
    private Long id;

    @Column(nullable = false)
    private String question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User user;

    @OneToMany(mappedBy = "mainCommentId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentReply> replies;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
