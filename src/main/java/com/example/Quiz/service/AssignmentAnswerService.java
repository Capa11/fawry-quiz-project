package com.example.Quiz.service;

import com.example.Quiz.model.Assignment;
import com.example.Quiz.model.AssignmentAnswer;
import com.example.Quiz.model.User;
import com.example.Quiz.payload.getUserMcqAnswersResponse;
import com.example.Quiz.repository.AssignmentAnswerRepository;
import com.example.Quiz.repository.AssignmentRepository;
import com.example.Quiz.repository.McqAnswerRepository;
import com.example.Quiz.security.AuthUser;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AssignmentAnswerService {
    private final AssignmentAnswerRepository assignmentAnswerRepository;
    private final AssignmentRepository assignmentRepository;
    private final McqAnswerRepository mcqAnswerRepository;

    @Transactional
    public Long createAssignmentAnswer(Long assignmentId) {
        if(!checkUserPermission(assignmentId))throw new EntityNotFoundException("This user is not in the same room of the assignment");
        AssignmentAnswer assignmentAnswer = AssignmentAnswer
                .builder()
                .assignment(Assignment.builder().id(assignmentId).build())
                .user(((AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser())
                .build();
        return assignmentAnswerRepository.save(assignmentAnswer).getId();
    }



    @Transactional(readOnly = true)
    public Boolean checkUserPermission(Long assignmentId){
        User user = ((AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        return assignmentRepository.checkUserPermission(user.getId(),assignmentId).isPresent();
    }


}
