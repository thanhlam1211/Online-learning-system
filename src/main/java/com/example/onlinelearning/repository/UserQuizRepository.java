package com.example.onlinelearning.repository;

import com.example.onlinelearning.entity.UserQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserQuizRepository extends JpaRepository<UserQuiz,Integer> {
    public List<UserQuiz> getUserQuizByQuiz_IdAndUser_Id(int quiz_id, int user_id);
}
