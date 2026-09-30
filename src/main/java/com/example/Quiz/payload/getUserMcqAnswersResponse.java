package com.example.Quiz.payload;

import java.util.List;

public record getUserMcqAnswersResponse(Long questionId, Short choiceNumber, Short questionNumber) {
}
