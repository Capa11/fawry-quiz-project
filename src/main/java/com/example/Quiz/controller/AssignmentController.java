// AssignmentController.java
package com.example.Quiz.controller;

import com.example.Quiz.service.AssignmentService;
import com.example.Quiz.model.*;
import com.example.Quiz.payload.AssignmentsListElement;
import com.example.Quiz.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/rooms/{roomId}/assignments")
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignmentService assignmentService;

    @GetMapping
    public ResponseEntity<Page<AssignmentsListElement>> getAssignmentsByRoom(@PathVariable Long roomId,Pageable pageable) {
        return ResponseEntity.ok(assignmentService.findAllByRoomId(roomId,pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Assignment>> searchAssignments(
            @PathVariable Long roomId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Pageable pageable) {

        return ResponseEntity.ok(assignmentService.searchAssignments(
                roomId, title, startDate, endDate, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Assignment> getAssignment(
            @PathVariable Long roomId,
            @PathVariable Long id,
            @AuthenticationPrincipal AuthUser currentUser) {

        return assignmentService.findById(roomId, id, currentUser.getUser())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Assignment> createAssignment(
            @PathVariable Long roomId,
            @RequestBody Assignment assignment,
            @AuthenticationPrincipal AuthUser currentUser) {

        return ResponseEntity.ok(assignmentService.createAssignment(roomId, assignment, currentUser.getUser()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Assignment> updateAssignment(
            @PathVariable Long roomId,
            @PathVariable Long id,
            @RequestBody Assignment assignmentDetails,
            @AuthenticationPrincipal AuthUser currentUser) {

        return ResponseEntity.ok(assignmentService.updateAssignment(roomId, id, assignmentDetails, currentUser.getUser()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(
            @PathVariable Long roomId,
            @PathVariable Long id,
            @AuthenticationPrincipal AuthUser currentUser) {

        assignmentService.deleteAssignment(roomId, id, currentUser.getUser());
        return ResponseEntity.ok().build();
    }

    //handle these 3 methods (check permissions and date )
    @GetMapping("/{id}/mcq-questions")
    public ResponseEntity<Page<McqQuestion>> getMcqQuestions(
            @PathVariable Long roomId,
            @PathVariable Long id,
            @ParameterObject Pageable pageable) {

        return ResponseEntity.ok(assignmentService.getMcqQuestions(roomId, id, pageable));
    }

    @GetMapping("/{id}/trueFalse-questions")
    public ResponseEntity<Page<TrueFalseQuestion>> getTrueFalseQuestions(
            @PathVariable Long roomId,
            @PathVariable Long id,
            @ParameterObject Pageable pageable) {

        return ResponseEntity.ok(assignmentService.getTrueFalseQuestions(roomId, id, pageable));
    }

    @GetMapping("/{id}/essay-questions")
    public ResponseEntity<Page<EssayQuestion>> getEssayQuestions(
            @PathVariable Long roomId,
            @PathVariable Long id,
            @ParameterObject Pageable pageable) {

        return ResponseEntity.ok(assignmentService.getEssayQuestions(roomId, id, pageable));
    }
}
