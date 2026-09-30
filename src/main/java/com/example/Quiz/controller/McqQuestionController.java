package com.example.Quiz.controller;

import com.example.Quiz.model.McqQuestion;
import com.example.Quiz.service.McqQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms/{roomId}/assignments/{assignmentId}/mcq-questions")
public class McqQuestionController {

    private final McqQuestionService mcqQuestionService;

    @PutMapping("/{id}")
    public ResponseEntity<McqQuestion> updateMcqQuestion(
            @PathVariable Long roomId,
            @PathVariable Long assignmentId,
            @PathVariable Long id,
            @RequestBody McqQuestion details) {
        return ResponseEntity.ok(mcqQuestionService.updateMcqQuestion(roomId, assignmentId, id, details));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMcqQuestion(
            @PathVariable Long roomId,
            @PathVariable Long assignmentId,
            @PathVariable Long id) {
        mcqQuestionService.deleteMcqQuestion(roomId, assignmentId, id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping
    public ResponseEntity<McqQuestion> createMcqQuestion(
            @PathVariable Long roomId,
            @PathVariable Long assignmentId,
            @RequestBody McqQuestion mcqQuestion) {
        McqQuestion savedQuestion = mcqQuestionService.createMcqQuestion(roomId,assignmentId,mcqQuestion);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedQuestion);
    }

}
