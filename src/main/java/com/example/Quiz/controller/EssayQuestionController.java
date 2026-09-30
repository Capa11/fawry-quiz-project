package com.example.Quiz.controller;

import com.example.Quiz.model.EssayQuestion;
import com.example.Quiz.service.EssayQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms/{roomId}/assignments/{assignmentId}/essay-questions")
public class EssayQuestionController {

    private final EssayQuestionService essayQuestionService;

    @PutMapping("/{id}")
    public ResponseEntity<EssayQuestion> updateEssayQuestion(
            @PathVariable Long roomId,
            @PathVariable Long assignmentId,
            @PathVariable Long id,
            @RequestBody EssayQuestion details) {
        return ResponseEntity.ok(essayQuestionService.updateEssayQuestion(roomId, assignmentId, id, details));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEssayQuestion(
            @PathVariable Long roomId,
            @PathVariable Long assignmentId,
            @PathVariable Long id) {
        essayQuestionService.deleteEssayQuestion(roomId, assignmentId, id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<EssayQuestion> createEssayQuestion(
            @PathVariable Long roomId,
            @PathVariable Long assignmentId,
            @RequestBody EssayQuestion essayQuestion) {
        EssayQuestion savedQuestion = essayQuestionService.createEssayQuestion(roomId,assignmentId,essayQuestion);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedQuestion);
    }
}
