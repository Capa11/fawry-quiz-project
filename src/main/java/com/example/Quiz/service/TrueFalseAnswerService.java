package com.example.Quiz.service;

import com.example.Quiz.model.*;
import com.example.Quiz.model.enums.Roles;
import com.example.Quiz.payload.getUserTrueFalseAnswersResponse;
import com.example.Quiz.repository.AssignmentAnswerRepository;
import com.example.Quiz.repository.RoomRoleRepository;
import com.example.Quiz.repository.TrueFalseAnswerRepository;
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
public class TrueFalseAnswerService {

    private final TrueFalseAnswerRepository trueFalseAnswerRepository;
    private final AssignmentAnswerRepository assignmentAnswerRepository;
    private final RoomRoleRepository roomRoleRepository;

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ((AuthUser) auth.getPrincipal()).getUser();
    }

    @Transactional
    public Map<String, Object> createCurrentUserAnswer(Long assignmentId, Long questionId, boolean answer) {
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

        TrueFalseQuestion question = new TrueFalseQuestion();
        question.setId(questionId);

        TrueFalseAnswer tfAnswer = new TrueFalseAnswer();
        tfAnswer.setAssignmentAnswer(assignmentAnswer);
        tfAnswer.setQuestion(question);
        tfAnswer.setAnswer(answer);

        trueFalseAnswerRepository.save(tfAnswer);

        Map<String, Object> response = new HashMap<>();
        response.put("questionId", questionId);
        response.put("answer", answer);

        return response;
    }

    @Transactional
    public Map<String, Object> updateCurrentUserAnswer(Long assignmentId, Long questionId, boolean newAnswer) {
        TrueFalseQuestion question = new TrueFalseQuestion();
        question.setId(questionId);

        trueFalseAnswerRepository.deleteByQuestionAndUserId(questionId,getCurrentUser().getId());

        return createCurrentUserAnswer(assignmentId, questionId, newAnswer);
    }

    @Transactional(readOnly = true)
    public Page<getUserTrueFalseAnswersResponse> getUserAnswersForAssignment(Long assignmentId, Pageable pageable) {
        User user = getCurrentUser();
        return trueFalseAnswerRepository.findUserTrueFalseAnswersForAssignment( user.getId(),assignmentId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<getUserTrueFalseAnswersResponse> adminGetUserAnswersForAssignment(Long assignmentId, Long userId, Pageable pageable) throws IllegalAccessException {
        User current = getCurrentUser();

        if (current.getRole().equals(Roles.ROLE_ADMIN) ||
            roomRoleRepository.findByUserIdAndAssignmentId(assignmentId, current.getId())
                              .orElseThrow()
                              .getCanSeeUserDetails()) {
            return trueFalseAnswerRepository.findUserTrueFalseAnswersForAssignment( userId,assignmentId, pageable);
        } else {
            throw new IllegalAccessException("This method can only be used by Admin or room Admins");
        }
    }
}
