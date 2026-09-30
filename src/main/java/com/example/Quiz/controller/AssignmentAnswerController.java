package com.example.Quiz.controller;


import com.example.Quiz.service.AssignmentAnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/answers")
@RequiredArgsConstructor
public class AssignmentAnswerController {
    private final AssignmentAnswerService assignmentAnswerService;

    @PostMapping("/{assignmentId}")
    public ResponseEntity<?> createAssignmentAnswer(@PathVariable Long assignmentId) {
        assignmentAnswerService.createAssignmentAnswer(assignmentId);
        return ResponseEntity.ok().build();
    }
}
