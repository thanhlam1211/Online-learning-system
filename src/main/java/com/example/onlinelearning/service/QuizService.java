package com.example.onlinelearning.service;

import com.example.onlinelearning.entity.Course;
import com.example.onlinelearning.entity.Quiz;
import com.example.onlinelearning.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class QuizService {
    @Autowired
    private QuizRepository quizRepository;

    public Page<Quiz> listAll(int currentPage, String keyword){
        Pageable pageable = PageRequest.of(currentPage - 1,6);
        if(keyword !=null){
            return quizRepository.findByNameContaining(keyword,pageable);
        }

        return quizRepository.findAll(pageable);
    }

    public Quiz getQuizById(int id){
        return quizRepository.getById(id);
    }

    public void saveQuiz(Quiz quiz){quizRepository.save(quiz);}
    public void deleteQuiz(Integer id){
        Quiz quiz = quizRepository.getById(id);
        quizRepository.delete(quiz);
    }
}
