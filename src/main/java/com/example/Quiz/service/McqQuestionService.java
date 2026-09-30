package com.example.Quiz.service;

import com.example.Quiz.model.*;
import com.example.Quiz.repository.AssignmentRepository;
import com.example.Quiz.repository.McqQuestionRepository;
import com.example.Quiz.repository.UserRoomRoleRepository;
import com.example.Quiz.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class McqQuestionService {

    private final McqQuestionRepository mcqQuestionRepository;
    private final UserRoomRoleRepository userRoomRoleRepository;
    private final AssignmentRepository assignmentRepository;

    @Transactional
    public McqQuestion updateMcqQuestion(Long roomId, Long assignmentId, Long id, McqQuestion details) {
        User user = ((AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        if (!hasUpdatePermission(user, roomId)) {
            throw new SecurityException("User has no permission to update MCQ questions in this room");
        }

        McqQuestion question = mcqQuestionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MCQ question not found"));

        Assignment assignment = question.getAssignment();
        if (!assignment.getId().equals(assignmentId)) {
            throw new RuntimeException("Question does not belong to the given assignment");
        }

        question.setQuestionBody(details.getQuestionBody());
        question.setImageUrl(details.getImageUrl());
        question.setMultipleAnswer(details.isMultipleAnswer());
        question.setPoints(details.getPoints());

        // Replace choices: remove old ones and attach new ones
        question.getMcqChoices().clear();
        int countCorrectAnswer = 0;
        if (details.getMcqChoices() != null) {
            for (McqChoice choice : details.getMcqChoices()) {
                choice.setMcqQuestion(question);
                if(choice.getIsCorrect())countCorrectAnswer++;
            }
            question.getMcqChoices().addAll(details.getMcqChoices());
        }
        if(question.isMultipleAnswer()&&countCorrectAnswer <=1) {throw new IllegalArgumentException("This Question must have multiple answers due to its settings");}
        if(!question.isMultipleAnswer()&&countCorrectAnswer !=1){throw new IllegalArgumentException("This Question must have only one answer due to its settings");}


        return mcqQuestionRepository.save(question);
    }

    @Transactional
    public void deleteMcqQuestion(Long roomId, Long assignmentId, Long id) {
        User user = ((AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        if (!hasUpdatePermission(user, roomId)) {
            throw new SecurityException("User has no permission to delete MCQ questions in this room");
        }

        McqQuestion question = mcqQuestionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MCQ question not found"));

        if (!question.getAssignment().getId().equals(assignmentId)) {
            throw new RuntimeException("Question does not belong to the given assignment");
        }

        mcqQuestionRepository.delete(question);
    }
    @Transactional
    public McqQuestion createMcqQuestion(Long roomId, Long assignmentId, McqQuestion mcqQuestion) {
        User user = ((AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        if (!hasUpdatePermission(user, roomId)) {
            throw new AccessDeniedException("User not allowed to add true/false question");
        }
        Assignment assignment = assignmentRepository.findById(assignmentId).orElseThrow();
        int countCorrectAnswer = 0;
        if(!assignment.isMcqExist()) throw new RuntimeException("this assignment doesn't allow mcq questions");
        mcqQuestion.setAssignment(assignment);
        for(McqChoice mcqChoice: mcqQuestion.getMcqChoices()){
            mcqChoice.setMcqQuestion(mcqQuestion);
            if(mcqChoice.getIsCorrect())countCorrectAnswer++;
        }


        if(mcqQuestion.isMultipleAnswer()&&countCorrectAnswer <=1) {throw new IllegalArgumentException("This Question must have multiple answers due to its settings");}
        if(!mcqQuestion.isMultipleAnswer()&&countCorrectAnswer !=1){throw new IllegalArgumentException("This Question must have only one answer due to its settings");}

        return mcqQuestionRepository.save(mcqQuestion);
    }
    private boolean hasUpdatePermission(User user, Long roomId) {
        return user.getRole().name().equals("ROLE_ADMIN") ||
                userRoomRoleRepository.findByUserIdAndRoomId(user.getId(), roomId)
                        .orElseThrow()
                        .getRoomRole()
                        .getCanAddAssignment();
    }
}
