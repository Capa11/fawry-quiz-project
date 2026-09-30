package com.example.Quiz.repository;

import com.example.Quiz.model.McqQuestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface McqQuestionRepository extends JpaRepository<McqQuestion, Long> {

    Page<McqQuestion> findByAssignmentId(Long assignmentId, Pageable pageable);

    @Query("SELECT q FROM McqQuestion q JOIN FETCH q.assignment a JOIN FETCH a.room r JOIN UserRoomRole urr ON urr.roomId = r.id WHERE :mcqId = q.id AND urr.user.id =:userId")
    McqQuestion findQuestionAndAscendants(Long mcqId, Long userId);

    @Query("""
    SELECT q
    FROM McqQuestion q
    JOIN q.assignment a
    JOIN a.room r 
    JOIN r.roomRoles rr
    JOIN rr.userRoomRoles urr
    WHERE :userId = urr.user.id
    AND q.id = :mcqId
""")
    Optional<McqQuestion> checkUserInRoom(@Param("mcqId") Long mcqId, @Param("userId") Long userId);

}
