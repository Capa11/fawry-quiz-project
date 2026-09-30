package com.example.Quiz.payload;

import java.util.List;

public record getUserEssayAnswersResponse(Long questionId, String answer, Short questionNumber) {
}