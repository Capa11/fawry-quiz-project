package com.example.Quiz.service;

import com.example.Quiz.model.*;
import com.example.Quiz.model.enums.Roles;
import com.example.Quiz.payload.getUserEssayAnswersResponse;
import com.example.Quiz.repository.AssignmentAnswerRepository;
import com.example.Quiz.repository.RoomRoleRepository;
import com.example.Quiz.repository.EssayAnswerRepository;
import com.example.Quiz.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EssayAnswerService {

    private final EssayAnswerRepository essayAnswerRepository;
    private final AssignmentAnswerRepository assignmentAnswerRepository;
    private final RoomRoleRepository roomRoleRepository;

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ((AuthUser) auth.getPrincipal()).getUser();
    }

    @Transactional
    public Map<String, Object> createCurrentUserAnswer(Long assignmentId, Long questionId, String answer) {
        User user = getCurrentUser();
        AssignmentAnswer assignmentAnswer = assignmentAnswerRepository
                .findByUser_IdAndAssignment_Id(user.getId(), assignmentId)
                .orElseThrow();

        Assignment assignment = assignmentAnswer.getAssignment();

        if (assignment.getExamDurationMinutes() != null &&
                assignmentAnswer.getStartDate()
                        .plusMinutes(assignment.getExamDurationMinutes())
                        .isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("The exam time ended");
        }

        EssayQuestion question = new EssayQuestion();
        question.setId(questionId);

        EssayAnswer esAnswer = new EssayAnswer();
        esAnswer.setAssignmentAnswer(assignmentAnswer);
        esAnswer.setQuestion(question);
        esAnswer.setAnswer(answer);

        essayAnswerRepository.save(esAnswer);

        Map<String, Object> response = new HashMap<>();
        response.put("questionId", questionId);
        response.put("answer", answer);

        return response;
    }

    @Transactional
    public Map<String, Object> updateCurrentUserAnswer(Long assignmentId, Long questionId, String newAnswer) {
        EssayQuestion question = new EssayQuestion();
        question.setId(questionId);

        essayAnswerRepository.deleteByQuestionAndUserId(questionId,getCurrentUser().getId());

        return createCurrentUserAnswer(assignmentId, questionId, newAnswer);
    }

    @Transactional(readOnly = true)
    public Page<getUserEssayAnswersResponse> getUserAnswersForAssignment(Long assignmentId, Pageable pageable) {
        User user = getCurrentUser();
        return essayAnswerRepository.findUserEssayAnswersForAssignment( user.getId(),assignmentId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<getUserEssayAnswersResponse> adminGetUserAnswersForAssignment(Long assignmentId, Long userId, Pageable pageable) throws IllegalAccessException {
        User current = getCurrentUser();

        if (current.getRole().equals(Roles.ROLE_ADMIN) ||
                roomRoleRepository.findByUserIdAndAssignmentId(assignmentId, current.getId())
                        .orElseThrow()
                        .getCanSeeUserDetails()) {
            return essayAnswerRepository.findUserEssayAnswersForAssignment( userId,assignmentId, pageable);
        } else {
            throw new IllegalAccessException("This method can only be used by Admin or room Admins");
        }
    }
}
