package com.example.Quiz.payload;

import java.util.List;

public record getUserTrueFalseAnswersResponse(Long questionId, Boolean answer, Short questionNumber) {
}