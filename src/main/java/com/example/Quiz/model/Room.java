package com.example.Quiz.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


@Entity
@Table(    name = "rooms",
        indexes = {
                @Index(name = "idx_room_name", columnList = "name")
        })
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min = 1, max = 100)
    private String name="No name";

    @Column(name = "join_by_link", nullable = false)
    private Boolean joinByLink = true;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_questions_allowed")
    private Boolean isQuestionAllowed = false;

    @Column(name = "is_comments_allowed")
    private Boolean isCommentAllowed = false;

    @Column(name = "is_verified")
    private Boolean isVerified=false;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "room", cascade = CascadeType.ALL)
    private List<RoomRole> roomRoles;


    @OneToMany(fetch = FetchType.LAZY,mappedBy = "room", cascade = CascadeType.ALL)
    private List<Assignment> assignments;

    public Room(String name, Boolean joinByLink, Boolean isQuestionAllowed, Boolean isCommentAllowed) {
        this.name = Objects.requireNonNull(name, "name cannot be null");
        this.joinByLink = Objects.requireNonNull(joinByLink, "joinByLink cannot be null");
        this.isQuestionAllowed = Objects.requireNonNull(isQuestionAllowed, "isQuestionsAllowed cannot be null");
        this.isCommentAllowed = Objects.requireNonNull(isCommentAllowed, "isCommentsAllowed cannot be null");


    }


}
