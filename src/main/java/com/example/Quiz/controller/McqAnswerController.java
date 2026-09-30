package com.example.Quiz.controller;

import com.example.Quiz.service.McqAnswerService;
import com.example.Quiz.payload.getUserMcqAnswersResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mcq-questions")
@RequiredArgsConstructor
public class McqAnswerController {

    private final McqAnswerService mcqAnswerService;

    @PutMapping("/{assignmentId}/{questionId}/answer")
    public ResponseEntity<Map<String, Object>> updateCurrentUserAnswer(
            @PathVariable Long questionId,
            @PathVariable Long assignmentId,
            @RequestBody List<Short> ChoiceNumbers) {
        Map<String, Object> answer = mcqAnswerService.updateCurrentUserAnswer(
                assignmentId,
                questionId,
                ChoiceNumbers
        );
        return ResponseEntity.ok(answer);
    }

    @PostMapping("/{assignmentId}/{questionId}/answer")
    public ResponseEntity<Map<String, Object>> createCurrentUserAnswer(
            @PathVariable Long questionId,
            @PathVariable Long assignmentId,
            @RequestBody List<Short> ChoiceNumbers) {
        Map<String, Object> answer = mcqAnswerService.createCurrentUserAnswer(
                assignmentId,
                questionId,
                ChoiceNumbers
        );
        return ResponseEntity.ok(answer);
    }


    @GetMapping("/{assignmentId}/answer/me")
    public ResponseEntity<Page<getUserMcqAnswersResponse>>
    getUserAnswersForAssignment(
            @PathVariable Long assignmentId,
            Pageable pageable) {
        return ResponseEntity.ok(mcqAnswerService.getUserMcqAnswersForAssignment(assignmentId,pageable));
    }
    @GetMapping("/{assignmentId}/answer/{userId}")
    public ResponseEntity<Page<getUserMcqAnswersResponse>>
    adminGetUserAnswersForAssignment(
            @PathVariable Long assignmentId,
            @PathVariable Long userId,
            Pageable pageable) throws IllegalAccessException {
        return ResponseEntity.ok(mcqAnswerService.adminGetUserMcqAnswersForAssignment(assignmentId,userId,pageable));
    }
}
