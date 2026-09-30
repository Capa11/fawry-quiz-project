package com.example.Quiz.controller;

import com.example.Quiz.model.TrueFalseQuestion;
import com.example.Quiz.service.TrueFalseQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms/{roomId}/assignments/{assignmentId}/truefalse-questions")
public class TrueFalseQuestionController {

    private final TrueFalseQuestionService trueFalseQuestionService;

    @PutMapping("/{id}")
    public ResponseEntity<TrueFalseQuestion> updateTrueFalseQuestion(
            @PathVariable Long roomId,
            @PathVariable Long assignmentId,
            @PathVariable Long id,
            @RequestBody TrueFalseQuestion details) {
        return ResponseEntity.ok(trueFalseQuestionService.updateTrueFalseQuestion(roomId, assignmentId, id, details));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrueFalseQuestion(
            @PathVariable Long roomId,
            @PathVariable Long assignmentId,
            @PathVariable Long id) {
        trueFalseQuestionService.deleteTrueFalseQuestion(roomId, assignmentId, id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<TrueFalseQuestion> createTrueFalseQuestion(
            @PathVariable Long roomId,
            @PathVariable Long assignmentId,
            @RequestBody TrueFalseQuestion trueFalseQuestion) {
        TrueFalseQuestion savedQuestion = trueFalseQuestionService.createTrueFalseQuestion(roomId,assignmentId,trueFalseQuestion);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedQuestion);
    }
}
