package com.example.Quiz.repository;

import com.example.Quiz.model.McqAnswer;
import com.example.Quiz.model.McqQuestion;
import com.example.Quiz.payload.getUserMcqAnswersResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface McqAnswerRepository extends JpaRepository<McqAnswer, Long> {
//    List<McqAnswer> findByMcqQuestion_Assignment_Id(Long assignmentId);
//
//    List<McqAnswer> findByUserAndMcqQuestion(User user, McqQuestion mcqQuestion);
//
//    Optional<McqAnswer> findByUserAndMcqQuestionAndChoiceNumber(User user, McqQuestion mcqQuestion, Short choiceNumber);

//    @Query("SELECT ma FROM McqAnswer ma JOIN  ma.mcqQuestion q JOIN  q.assignment a JOIN  a.room r JOIN UserRoomRole urr ON urr.roomId = r.id WHERE :mcqId = q.id AND urr.user.id =:userId AND ma.user.id = :userId")
//    List<McqAnswer> findAnswerAndAscendants(Long mcqId, Long userId);

//    @Modifying
//    @Query("DELETE FROM McqAnswer ma WHERE ma.user.id = :userId AND ma.mcqQuestion.id = :mcqId")
//    int deleteAnswersByUserAndQuestion(@Param("userId") Long userId, @Param("mcqId") Long mcqId);

//    @Query("""
//    SELECT ans
//    FROM McqAnswer ans
//    JOIN FETCH ans.mcqQuestion q
//    JOIN q.assignment a
//    WHERE a.id = :assignmentId
//    AND ans.user.id = :userId
//    ORDER BY q.questionNumber
//""")
//    List<McqAnswer> getUserMcqAnswersForAssignment(@Param("userId") Long userId, @Param("assignmentId") Long assignmentId);

    @Query("""
    SELECT new com.example.Quiz.payload.getUserMcqAnswersResponse(
        q.id,
        ma.choiceNumber,
        q.questionNumber
    )
    FROM McqAnswer ma
    JOIN ma.mcqQuestion q
    JOIN ma.assignmentAnswer aa
    WHERE aa.user.id = :userId AND aa.assignment.id = :assignmentId
    ORDER BY q.questionNumber
""")
    Page<getUserMcqAnswersResponse> findUserMcqAnswersForAssignment(
            @Param("userId") Long userId,
            @Param("assignmentId") Long assignmentId,
            Pageable pageable
    );


    @Modifying
    @Query("""
    DELETE FROM McqAnswer a
    WHERE a.mcqQuestion.id = :mcqQuestionId
    AND a.assignmentAnswer.user.id = :userId
""")
    int deleteByMcqQuestionAndUserId(@Param("mcqQuestionId") Long mcqQuestionId,
                                     @Param("userId") Long userId);


//    @Query(value = """
//        SELECT ma
//        FROM McqAnswer ma
//        JOIN ma.assignmentAnswer aa
//        WHERE aa.user.id = :userId AND aa.assignment.id = :assignmentId
//        ORDER BY ma.questionNumber
//    """
//    )
//    Page<McqAnswer> findUserMcqAnswersForQuestion(@Param("userId") Long userId,
//                                       @Param("assignmentId") Long assignmentId,
//                                       Pageable pageable);
}