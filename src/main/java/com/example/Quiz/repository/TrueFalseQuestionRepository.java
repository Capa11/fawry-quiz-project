package com.example.Quiz.repository;

import com.example.Quiz.model.TrueFalseQuestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrueFalseQuestionRepository extends JpaRepository<TrueFalseQuestion, Long> {
    Page<TrueFalseQuestion> findByAssignmentId(Long assignmentId, Pageable pageable);
}
