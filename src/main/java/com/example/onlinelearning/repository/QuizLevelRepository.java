package com.example.onlinelearning.repository;

import com.example.onlinelearning.entity.QuizLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizLevelRepository extends JpaRepository<QuizLevel,Integer> {
}
