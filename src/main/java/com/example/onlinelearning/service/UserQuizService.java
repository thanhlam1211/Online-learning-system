package com.example.onlinelearning.service;

import com.example.onlinelearning.entity.UserQuiz;
import com.example.onlinelearning.repository.UserQuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserQuizService {

    @Autowired
    private UserQuizRepository userQuizRepository;

    public List<UserQuiz> getUserQuizByQuiz_IdAndUser_Id(int quiz_id, int user_id){
        return userQuizRepository.getUserQuizByQuiz_IdAndUser_Id(quiz_id,user_id);
    }
}
