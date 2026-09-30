package com.example.Quiz.repository;

import com.example.Quiz.model.TrueFalseAnswer;
import com.example.Quiz.payload.getUserMcqAnswersResponse;
import com.example.Quiz.payload.getUserTrueFalseAnswersResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TrueFalseAnswerRepository extends JpaRepository<TrueFalseAnswer, Long> {
    @Modifying
    @Query("""
    DELETE FROM TrueFalseAnswer a
    WHERE a.question.id = :questionId
    AND a.assignmentAnswer.user.id = :userId
""")
    int deleteByQuestionAndUserId(@Param("questionId") Long questionId,
                                     @Param("userId") Long userId);


    @Query("""
    SELECT new com.example.Quiz.payload.getUserTrueFalseAnswersResponse(
        q.id,
        a.answer,
        q.questionNumber
    )
    FROM TrueFalseAnswer a
    JOIN a.question q
    JOIN a.assignmentAnswer aa
    WHERE aa.user.id = :userId AND aa.assignment.id = :assignmentId
    ORDER BY q.questionNumber
""")
    Page<getUserTrueFalseAnswersResponse> findUserTrueFalseAnswersForAssignment(
            @Param("userId") Long userId,
            @Param("assignmentId") Long assignmentId,
            Pageable pageable
    );
}
