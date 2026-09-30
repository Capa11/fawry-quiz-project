package com.example.Quiz.controller;

import com.example.Quiz.service.EssayAnswerService;
import com.example.Quiz.payload.getUserEssayAnswersResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/essay-questions")
@RequiredArgsConstructor
public class EssayAnswerController {

    private final EssayAnswerService essayAnswerService;

    @PostMapping("/{assignmentId}/{questionId}/answer")
    public ResponseEntity<Map<String, Object>> createCurrentUserAnswer(
            @PathVariable Long assignmentId,
            @PathVariable Long questionId,
            @RequestBody String answer) {
        return ResponseEntity.ok(
                essayAnswerService.createCurrentUserAnswer(assignmentId, questionId, answer)
        );
    }

    @PutMapping("/{assignmentId}/{questionId}/answer")
    public ResponseEntity<Map<String, Object>> updateCurrentUserAnswer(
            @PathVariable Long assignmentId,
            @PathVariable Long questionId,
            @RequestBody String newAnswer) {
        return ResponseEntity.ok(
                essayAnswerService.updateCurrentUserAnswer(assignmentId, questionId, newAnswer)
        );
    }

    @GetMapping("/{assignmentId}/answer/me")
    public ResponseEntity<Page<getUserEssayAnswersResponse>> getUserAnswersForAssignment(
            @PathVariable Long assignmentId,
            Pageable pageable) {
        return ResponseEntity.ok(
                essayAnswerService.getUserAnswersForAssignment(assignmentId, pageable)
        );
    }

    @GetMapping("/{assignmentId}/answer/{userId}")
    public ResponseEntity<Page<getUserEssayAnswersResponse>> adminGetUserAnswersForAssignment(
            @PathVariable Long assignmentId,
            @PathVariable Long userId,
            Pageable pageable) throws IllegalAccessException {
        return ResponseEntity.ok(
                essayAnswerService.adminGetUserAnswersForAssignment(assignmentId, userId, pageable)
        );
    }
}
