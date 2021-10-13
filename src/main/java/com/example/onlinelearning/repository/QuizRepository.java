package com.example.onlinelearning.repository;

import com.example.onlinelearning.entity.Quiz;
import com.example.onlinelearning.entity.QuizLevel;
import com.example.onlinelearning.entity.QuizType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepository extends JpaRepository<Quiz,Integer> {
    public Page<Quiz> findQuizzesByNameContaining(String name, Pageable pageable);
    public Page<Quiz> findQuizzesByQuizLevelId(Integer quizLevel_id, Pageable pageable);
    public Page<Quiz> findQuizzesByNameContainingAndQuizLevelId(String name, Integer quizLevel_id, Pageable pageable);
    public Page<Quiz> findQuizzesByQuizTypeId(Integer quizType_id, Pageable pageable);
    public Page<Quiz> findQuizzesByNameContainingAndQuizTypeId(String name, Integer quizType_id, Pageable pageable);
    public Page<Quiz> findQuizzesByQuizLevelIdAndQuizTypeId(Integer quizLevel_id, Integer quizType_id, Pageable pageable);
    public Page<Quiz> findQuizzesByNameContainingAndQuizLevelIdAndQuizTypeId(String name, Integer quizLevel_id, Integer quizType_id, Pageable pageable);
}
