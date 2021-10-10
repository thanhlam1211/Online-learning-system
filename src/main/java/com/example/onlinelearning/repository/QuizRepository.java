package com.example.onlinelearning.repository;

import com.example.onlinelearning.entity.Quiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepository extends JpaRepository<Quiz,Integer> {
    public Page<Quiz> findByNameContaining(String name, Pageable pageable);
}
