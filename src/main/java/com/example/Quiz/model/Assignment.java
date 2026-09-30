package com.example.Quiz.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "assignments", indexes = {
        @Index(name = "idx_assignments_title", columnList = "title"),
        @Index(name = "idx_assignments_room", columnList = "room_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @Column( nullable = false)
    @Size(min = 1, max = 100)
    private String title;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private Room room;

    @JoinColumn(name = "mcq_exist", nullable = false)
    private boolean mcqExist;

    @JoinColumn(name = "true_false_exist", nullable = false)
    private boolean trueFalseExist;

    @JoinColumn(name = "mcq_exist", nullable = false)
    private boolean essayExist;

    @Column(name = "exam_duration_minutes", nullable = false)
    private Long examDurationMinutes;

    @Column(name = "answer_showed_after_submission")
    private Boolean answerShowedAfterSubmission;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<McqQuestion> mcqQuestions;

    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrueFalseQuestion> trueFalseQuestions;
    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EssayQuestion> essayQuestions;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @JsonIgnore
    public Room getRoom() {
        return room;
    }
}
