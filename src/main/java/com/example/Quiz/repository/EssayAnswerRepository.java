package com.example.Quiz.repository;

import com.example.Quiz.model.EssayAnswer;
import com.example.Quiz.model.EssayQuestion;
import com.example.Quiz.model.User;
import com.example.Quiz.payload.getUserEssayAnswersResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EssayAnswerRepository extends JpaRepository<EssayAnswer, Long> {
    @Modifying
    @Query("""
    DELETE FROM EssayAnswer a
    WHERE a.question.id = :questionId
    AND a.assignmentAnswer.user.id = :userId
""")
    int deleteByQuestionAndUserId(@Param("questionId") Long questionId,
                                  @Param("userId") Long userId);


    @Query("""
    SELECT new com.example.Quiz.payload.getUserEssayAnswersResponse(
        q.id,
        a.answer,
        q.questionNumber
    )
    FROM EssayAnswer a
    JOIN a.question q
    JOIN a.assignmentAnswer aa
    WHERE aa.user.id = :userId AND aa.assignment.id = :assignmentId
    ORDER BY q.questionNumber
""")
    Page<getUserEssayAnswersResponse> findUserEssayAnswersForAssignment(
            @Param("userId") Long userId,
            @Param("assignmentId") Long assignmentId,
            Pageable pageable
    );
}