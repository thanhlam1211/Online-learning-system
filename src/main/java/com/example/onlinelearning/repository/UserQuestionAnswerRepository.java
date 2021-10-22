package com.example.onlinelearning.repository;

import com.example.onlinelearning.entity.UserQuestionAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserQuestionAnswerRepository extends JpaRepository<UserQuestionAnswer,Integer> {
    public List<UserQuestionAnswer> getUserQuestionAnswersByUserQuiz_IdOrderById(Integer userQuiz_id);
}
