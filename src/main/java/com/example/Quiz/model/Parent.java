package com.example.Quiz.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

@Table(name = "parent")
@Entity
@Getter @Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@IdClass(Parent.ParentId.class)
public class Parent {

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "parent_id")
    private User parent;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id")
    private User child;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public static class ParentId implements Serializable {
        private Long parent;
        private Long child;

    }
}
