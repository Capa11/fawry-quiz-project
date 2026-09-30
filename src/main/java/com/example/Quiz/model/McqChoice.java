package com.example.Quiz.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

@Entity
@Table(name = "mcq_choices", indexes = {
        @Index(name = "idx_mcq_choices_question", columnList = "mcq_question_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class McqChoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mcq_question_id", nullable = false)
    private McqQuestion mcqQuestion;

    @Column(name = "choice_text", columnDefinition = "TEXT")
    @Size(min = 1, max = 4000)
    private String choiceText;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect=false;

    @Column(name = "choice_number", nullable = false)
    private Short choiceNumber;


}
