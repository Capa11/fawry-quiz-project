package com.example.Quiz.service;

import com.example.Quiz.model.EssayQuestion;
import com.example.Quiz.model.Assignment;
import com.example.Quiz.model.User;
import com.example.Quiz.model.enums.Roles;
import com.example.Quiz.repository.AssignmentRepository;
import com.example.Quiz.repository.EssayQuestionRepository;
import com.example.Quiz.repository.UserRoomRoleRepository;
import com.example.Quiz.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EssayQuestionService {

    private final EssayQuestionRepository essayQuestionRepository;
    private final AssignmentRepository assignmentRepository;
    private final UserRoomRoleRepository userRoomRoleRepository;

    @Transactional
    public EssayQuestion updateEssayQuestion(Long roomId, Long assignmentId, Long id, EssayQuestion details) {
        User user = ((AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        if (!hasUpdatePermission(user, roomId)) {
            throw new SecurityException("User has no permission to update essay questions in this room");
        }

        EssayQuestion question = essayQuestionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Essay question not found"));

        if (!question.getAssignment().getId().equals(assignmentId)) {
            throw new RuntimeException("Question does not belong to the given assignment");
        }

        question.setQuestionNumber(details.getQuestionNumber());
        question.setQuestionBody(details.getQuestionBody());
        question.setEssayCorrectAnswer(details.getEssayCorrectAnswer());
        question.setImageUrl(details.getImageUrl());
        question.setPoints(details.getPoints());

        return essayQuestionRepository.save(question);
    }

    @Transactional
    public void deleteEssayQuestion(Long roomId, Long assignmentId, Long id) {
        User user = ((AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        if (!hasUpdatePermission(user, roomId)) {
            throw new SecurityException("User has no permission to delete essay questions in this room");
        }

        EssayQuestion question = essayQuestionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Essay question not found"));

        if (!question.getAssignment().getId().equals(assignmentId)) {
            throw new RuntimeException("Question does not belong to the given assignment");
        }

        essayQuestionRepository.delete(question);
    }
    @Transactional
    public EssayQuestion createEssayQuestion(Long roomId, Long assignmentId,EssayQuestion essayQuestion) {
        User user = ((AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        if (!hasUpdatePermission(user, roomId)) {
            throw new AccessDeniedException("User not allowed to add essay question");
        }
        Assignment assignment = assignmentRepository.findById(assignmentId).orElseThrow();
        if(!assignment.isEssayExist()) throw new RuntimeException("this assignment doesn't allow essay questions");
        essayQuestion.setAssignment(assignment);
        return essayQuestionRepository.save(essayQuestion);
    }
    private boolean hasUpdatePermission(User user, Long roomId) {
        return user.getRole().equals(Roles.ROLE_ADMIN) ||
                userRoomRoleRepository.findByUserIdAndRoomId(user.getId(), roomId)
                        .orElseThrow()
                        .getRoomRole()
                        .getCanAddAssignment();
    }
}
