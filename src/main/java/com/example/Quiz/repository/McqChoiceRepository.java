package com.example.Quiz.repository;

import com.example.Quiz.model.McqChoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface McqChoiceRepository extends JpaRepository<McqChoice, Long> {
}