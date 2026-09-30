package com.example.Quiz.repository;

import java.util.List;

public record getUserEssayAnswersResponse(Long questionId, String answer, Short questionNumber) {
}