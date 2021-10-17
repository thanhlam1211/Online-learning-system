package com.example.onlinelearning.repository;


import com.example.onlinelearning.entity.QuizLevel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Admin
 */
public interface LevelRepository extends JpaRepository<QuizLevel, Integer> {
}
