package com.example.onlinelearning.repository;

import com.example.onlinelearning.entity.QuizType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizTypeRepository extends JpaRepository<QuizType,Integer> {
}
