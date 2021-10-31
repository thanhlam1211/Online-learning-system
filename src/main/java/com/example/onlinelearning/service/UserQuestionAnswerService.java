package com.example.onlinelearning.service;

import com.example.onlinelearning.entity.UserQuestionAnswer;
import com.example.onlinelearning.repository.UserQuestionAnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserQuestionAnswerService {
    @Autowired
    private UserQuestionAnswerRepository userQuestionAnswerRepository;

    public List<UserQuestionAnswer> getUserQuestionAnswersByUserQuizOrderById(int userQuiz_id){
        return userQuestionAnswerRepository.getUserQuestionAnswersByUserQuiz_IdOrderById(userQuiz_id);
    }
}
