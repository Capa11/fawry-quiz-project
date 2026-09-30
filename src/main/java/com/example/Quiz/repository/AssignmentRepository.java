package com.example.Quiz.repository;

import com.example.Quiz.model.Assignment;
import com.example.Quiz.model.UserRoomRole;
import com.example.Quiz.payload.AssignmentsListElement;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long>, JpaSpecificationExecutor<Assignment> {
    @Query("SELECT new com.example.Quiz.payload.AssignmentsListElement( a.id, a.title, a.description) FROM Assignment a WHERE :roomId = a.room.id")
    Page<AssignmentsListElement> findByRoomId(@Param("roomId") Long roomId, Pageable pageable);

    List<Assignment> findByTitleAndRoom_Id(@Size(min = 1, max = 100) String title, Long roomId);

//    @Query("SELECT a FROM Assignment a WHERE a.room.id = :roomId AND a.dueDate BETWEEN :start AND :end")
//    List<Assignment> findAssignmentsByRoomAndDueDateRange(
//        @Param("roomId") Long roomId,
//        @Param("start") LocalDateTime start,
//        @Param("end") LocalDateTime end);
        
    long countByRoomId(Long roomId);


    @Query("""
                SELECT urr.id
                FROM UserRoomRole urr
                JOIN Assignment a ON urr.roomId = a.room.id
                WHERE a.id = :assignmentId
                AND urr.user.id = :userId
                """)
    Optional<Long> checkUserPermission(@Param("userId") Long userId, @Param("assignmentId")  Long assignmentId);
}