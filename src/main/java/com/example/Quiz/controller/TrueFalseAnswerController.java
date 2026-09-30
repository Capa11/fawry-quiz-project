package com.example.Quiz.controller;

import com.example.Quiz.service.TrueFalseAnswerService;
import com.example.Quiz.payload.getUserTrueFalseAnswersResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/true-false-questions")
@RequiredArgsConstructor
public class TrueFalseAnswerController {

    private final TrueFalseAnswerService trueFalseAnswerService;

    @PostMapping("/{assignmentId}/{questionId}/answer")
    public ResponseEntity<Map<String, Object>> createCurrentUserAnswer(
            @PathVariable Long assignmentId,
            @PathVariable Long questionId,
            @RequestBody boolean answer) {
        return ResponseEntity.ok(
                trueFalseAnswerService.createCurrentUserAnswer(assignmentId, questionId, answer)
        );
    }

    @PutMapping("/{assignmentId}/{questionId}/answer")
    public ResponseEntity<Map<String, Object>> updateCurrentUserAnswer(
            @PathVariable Long assignmentId,
            @PathVariable Long questionId,
            @RequestBody boolean newAnswer) {
        return ResponseEntity.ok(
                trueFalseAnswerService.updateCurrentUserAnswer(assignmentId, questionId, newAnswer)
        );
    }

    @GetMapping("/{assignmentId}/answer/me")
    public ResponseEntity<Page<getUserTrueFalseAnswersResponse>> getUserAnswersForAssignment(
            @PathVariable Long assignmentId,
            Pageable pageable) {
        return ResponseEntity.ok(
                trueFalseAnswerService.getUserAnswersForAssignment(assignmentId, pageable)
        );
    }

    @GetMapping("/{assignmentId}/answer/{userId}")
    public ResponseEntity<Page<getUserTrueFalseAnswersResponse>> adminGetUserAnswersForAssignment(
            @PathVariable Long assignmentId,
            @PathVariable Long userId,
            Pageable pageable) throws IllegalAccessException {
        return ResponseEntity.ok(
                trueFalseAnswerService.adminGetUserAnswersForAssignment(assignmentId, userId, pageable)
        );
    }
}
