package com.example.Quiz.repository;

import com.example.Quiz.model.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParentRepository extends JpaRepository<Parent, Parent.ParentId> {
}
