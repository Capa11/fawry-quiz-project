package com.example.Quiz.payload;

import com.example.Quiz.model.McqAnswer;
import com.example.Quiz.model.McqQuestion;
import lombok.AllArgsConstructor;
import lombok.*;

@Getter @Setter
@AllArgsConstructor
public class McqAnswerWithQuestionDTO {
    private McqQuestion question;
    private McqAnswer answer;
}
