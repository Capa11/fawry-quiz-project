package com.example.Quiz.service;

import com.example.Quiz.model.*;
import com.example.Quiz.repository.AssignmentRepository;
import com.example.Quiz.repository.RoomRepository;
import com.example.Quiz.repository.TrueFalseQuestionRepository;
import com.example.Quiz.repository.UserRoomRoleRepository;
import com.example.Quiz.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TrueFalseQuestionService {

    private final TrueFalseQuestionRepository trueFalseQuestionRepository;
    private final UserRoomRoleRepository userRoomRoleRepository;
    private final RoomRepository roomRepository;
    private final AssignmentRepository assignmentRepository;

    @Transactional
    public TrueFalseQuestion updateTrueFalseQuestion(Long roomId, Long assignmentId, Long id, TrueFalseQuestion details) {
        User user = ((AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        if (!hasUpdatePermission(user, roomId)) {
            throw new SecurityException("User has no permission to update True/False questions in this room");
        }

        TrueFalseQuestion question = trueFalseQuestionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("True/False question not found"));

        if (!question.getAssignment().getId().equals(assignmentId)) {
            throw new RuntimeException("Question does not belong to the given assignment");
        }

        question.setQuestionBody(details.getQuestionBody());
        question.setAnswer(details.isAnswer());
        question.setImageUrl(details.getImageUrl());
        question.setPoints(details.getPoints());

        return trueFalseQuestionRepository.save(question);
    }

    @Transactional
    public void deleteTrueFalseQuestion(Long roomId, Long assignmentId, Long id) {
        User user = ((AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        if (!hasUpdatePermission(user, roomId)) {
            throw new SecurityException("User has no permission to delete True/False questions in this room");
        }

        TrueFalseQuestion question = trueFalseQuestionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("True/False question not found"));

        if (!question.getAssignment().getId().equals(assignmentId)) {
            throw new RuntimeException("Question does not belong to the given assignment");
        }

        trueFalseQuestionRepository.delete(question);
    }
    @Transactional
    public TrueFalseQuestion createTrueFalseQuestion(Long roomId, Long assignmentId, TrueFalseQuestion trueFalseQuestion) {
        User user = ((AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        if (!hasUpdatePermission(user, roomId)) {
            throw new AccessDeniedException("User not allowed to add true/false question");
        }
        Assignment assignment = assignmentRepository.findById(assignmentId).orElseThrow();
        if(!assignment.isTrueFalseExist()) throw new RuntimeException("this assignment doesn't allow True/False questions");

        trueFalseQuestion.setAssignment(assignment);
        return trueFalseQuestionRepository.save(trueFalseQuestion);
    }
    private boolean hasUpdatePermission(User user, Long roomId) {
        return user.getRole().name().equals("ROLE_ADMIN") ||
                userRoomRoleRepository.findByUserIdAndRoomId(user.getId(), roomId)
                        .orElseThrow()
                        .getRoomRole()
                        .getCanAddAssignment();
    }
}
