package com.example.Quiz.service;

import com.example.Quiz.model.*;
import com.example.Quiz.model.enums.Roles;
import com.example.Quiz.payload.AssignmentsListElement;
import com.example.Quiz.repository.AssignmentRepository;
import com.example.Quiz.repository.McqQuestionRepository;
import com.example.Quiz.repository.TrueFalseQuestionRepository;
import com.example.Quiz.repository.EssayQuestionRepository;
import com.example.Quiz.repository.RoomRepository;
import com.example.Quiz.repository.UserRoomRoleRepository;
import com.example.Quiz.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final UserRoomRoleRepository userRoomRoleRepository;
    private final RoomRepository roomRepository;
    private final McqQuestionRepository mcqQuestionRepository;
    private final TrueFalseQuestionRepository trueFalseQuestionRepository;
    private final EssayQuestionRepository essayQuestionRepository;

    @Transactional(readOnly = true)
    public Optional<Assignment> findById(Long roomId, Long id, User currentUser) {
        if (!isUserInRoom(currentUser, roomId)) {
            throw new IllegalArgumentException("User is not in room");
        }

        return assignmentRepository.findById(id)
                .filter(assignment -> assignment.getRoom() != null
                        && roomId.equals(assignment.getRoom().getId()));
    }

    @Transactional(readOnly = true)
    public Page<AssignmentsListElement> findAllByRoomId(Long roomId, Pageable pageable) {
        User user = ((AuthUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        if(isUserInRoom(user,roomId)) return assignmentRepository.findByRoomId(roomId, pageable);
        else throw new IllegalArgumentException("User is not in room");
    }

    @Transactional(readOnly = true)
    public Page<Assignment> searchAssignments(Long roomId, String title, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        User user = ((AuthUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        if(isUserInRoom(user,roomId)) {
            Specification<Assignment> spec = (root, query, cb) -> {

                List<Predicate> predicates = new ArrayList<>();

                predicates.add(cb.equal(root.get("room").get("id"), roomId));

                if (title != null && !title.isEmpty()) {
                    predicates.add(cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
                }
                if (startDate != null) {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), startDate));
                }
                if (endDate != null) {
                    predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), endDate));
                }

                return cb.and(predicates.toArray(new Predicate[0]));
            };

            return assignmentRepository.findAll(spec, pageable);
        }
        else throw new IllegalArgumentException("User is not in room");
    }

    @Transactional
    public Assignment createAssignment(Long roomId, Assignment assignment, User currentUser) {
        if (!hasUpdateAssignmentPermission(currentUser, roomId)) {
            throw new SecurityException("User does not have permission to create assignments in this room");
        }

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        assignment.setRoom(room);

        return assignmentRepository.save(assignment);
    }

    @Transactional
    public Assignment updateAssignment(Long roomId, Long id, Assignment assignmentDetails, User currentUser) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found in the specified room"));

        if (!hasUpdateAssignmentPermission(currentUser, roomId)) {
            throw new SecurityException("User does not have permission to update assignments in this room");
        }

        assignment.setTitle(assignmentDetails.getTitle());
        assignment.setMcqExist(assignmentDetails.isMcqExist());
        assignment.setTrueFalseExist(assignmentDetails.isTrueFalseExist());
        assignment.setEssayExist(assignmentDetails.isEssayExist());
        assignment.setExamDurationMinutes(assignmentDetails.getExamDurationMinutes());
        assignment.setAnswerShowedAfterSubmission(assignmentDetails.getAnswerShowedAfterSubmission());
        assignment.setStartDate(assignmentDetails.getStartDate());
        assignment.setUpdatedAt(LocalDateTime.now());

        return assignmentRepository.save(assignment);
    }

    @Transactional
    public void deleteAssignment(Long roomId, Long id, User currentUser) {
        if (!hasUpdateAssignmentPermission(currentUser, roomId)) {
            throw new SecurityException("User does not have permission to delete assignments in this room");
        }
        assignmentRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<McqQuestion> getMcqQuestions(Long roomId, Long assignmentId, Pageable pageable) {
        User user = ((AuthUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        if(isUserInRoom(user,roomId)) {
            checkAssignmentExists(assignmentId);
            return mcqQuestionRepository.findByAssignmentId(assignmentId, pageable);
        }
        else throw new IllegalArgumentException("User is not in room");
    }

    @Transactional(readOnly = true)
    public Page<TrueFalseQuestion> getTrueFalseQuestions(Long roomId, Long assignmentId, Pageable pageable) {
        User user = ((AuthUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        if(isUserInRoom(user,roomId)) {
            checkAssignmentExists(assignmentId);
            return trueFalseQuestionRepository.findByAssignmentId(assignmentId, pageable);
        }
        else throw new IllegalArgumentException("User is not in room");
    }

    @Transactional(readOnly = true)
    public Page<EssayQuestion> getEssayQuestions(Long roomId, Long assignmentId, Pageable pageable) {
        User user = ((AuthUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        if(isUserInRoom(user,roomId)) {
            checkAssignmentExists(assignmentId);
            return essayQuestionRepository.findByAssignmentId(assignmentId, pageable);
        }
        else throw new IllegalArgumentException("User is not in room");
    }

    private void checkAssignmentExists(Long assignmentId) {
        if (!assignmentRepository.existsById(assignmentId)) {
            throw new RuntimeException("Assignment not found in the specified room");
        }
    }

    private boolean hasUpdateAssignmentPermission(User user, Long roomId) {

        return user.getRole().equals(Roles.ROLE_ADMIN)||
                userRoomRoleRepository.findByUserIdAndRoomId(user.getId(), roomId)
                        .orElseThrow()
                        .getRoomRole()
                        .getCanAddAssignment();
    }
    private boolean isUserInRoom(User user, Long roomId) {
        return user.getRole().equals(Roles.ROLE_ADMIN)||userRoomRoleRepository.findByUserIdAndRoomId(user.getId(),roomId).isPresent();
    }
}
