package com.example.Quiz.service;

import com.example.Quiz.model.*;
import com.example.Quiz.model.enums.Roles;
import com.example.Quiz.payload.getUserMcqAnswersResponse;
import com.example.Quiz.repository.AssignmentAnswerRepository;
import com.example.Quiz.repository.McqAnswerRepository;
import com.example.Quiz.repository.RoomRoleRepository;
import com.example.Quiz.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class McqAnswerService {

    private final McqAnswerRepository mcqAnswerRepository;
    private final AssignmentAnswerRepository assignmentAnswerRepository;
    private final RoomRoleRepository roomRoleRepository;

    @Transactional(readOnly = true)
    public Page<getUserMcqAnswersResponse> getUserMcqAnswersForAssignment(Long AssignmentId, Pageable pageable) {
        User user = getCurrentUser();
        return mcqAnswerRepository.findUserMcqAnswersForAssignment(user.getId(),AssignmentId,pageable);
    }
    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ((AuthUser) auth.getPrincipal()).getUser();
    }

    @Transactional
    public Map<String, Object> createCurrentUserAnswer(Long assignmentId, Long questionId, List<Short> choiceNumbers) {
        List<McqAnswer> mcqAnswers = new ArrayList<>(choiceNumbers.size());
        User user = getCurrentUser();
        AssignmentAnswer assignmentAnswer = assignmentAnswerRepository.findByUser_IdAndAssignment_Id(user.getId(),assignmentId).orElseThrow();
        Assignment assignment = assignmentAnswer.getAssignment();

        if(assignment.getExamDurationMinutes() != null &&
                assignmentAnswer.getStartDate()
                        .plusMinutes(assignment.getExamDurationMinutes())
                        .isBefore(LocalDateTime.now())){
            throw new IllegalStateException("The exam time ended");
        }
        McqQuestion mcqQuestion = McqQuestion.builder().id(questionId).build();
        Map<String, Object> response = new HashMap<>();

        for (Short choiceNumber : choiceNumbers) {
            mcqAnswers.add(McqAnswer.builder().mcqQuestion(mcqQuestion).choiceNumber(choiceNumber).assignmentAnswer(assignmentAnswer).build());
            response.putAll(Map.of("questionId",questionId,"choiceNumber",choiceNumber));
        }
        mcqAnswerRepository.saveAll(mcqAnswers);
        return response;
    }
    @Transactional
    public Map<String, Object> updateCurrentUserAnswer(Long assignmentId,Long questionId, List<Short> updatedChoiceNumbers) {
        mcqAnswerRepository.deleteByMcqQuestionAndUserId(questionId,getCurrentUser().getId());
        return createCurrentUserAnswer(assignmentId,questionId,updatedChoiceNumbers);
    }

    @Transactional(readOnly = true)
    public Page<getUserMcqAnswersResponse> adminGetUserMcqAnswersForAssignment(Long assignmentId, Long userId, Pageable pageable) throws IllegalAccessException {
        if(getCurrentUser().getRole().equals(Roles.ROLE_ADMIN)||roomRoleRepository.findByUserIdAndAssignmentId(assignmentId,getCurrentUser().getId()).orElseThrow().getCanSeeUserDetails())
        return mcqAnswerRepository.findUserMcqAnswersForAssignment(userId,assignmentId,pageable);
        else throw new IllegalAccessException("This method can only be used from Admin or room Admins");
    }
}
