package com.example.onlinelearning.service;

/**
 * @author Admin
 */

import com.example.onlinelearning.entity.QuestionBank;
import com.example.onlinelearning.repository.QuestionBankRepository;
import com.example.onlinelearning.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class QuestionService {
    @Autowired
    private QuestionBankRepository repository;
    @Autowired
    private QuizRepository quizRepository;

    public Page<QuestionBank> listAll(int pageNumber, String keyword, Integer courseId, Integer statusId ) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 10);
        if(!keyword.equals("")) {
            return repository.findByContentContaining(keyword, pageable);
        }
        if (courseId == -1 && statusId == -1) {
            return repository.findAll(pageable);
        }
        //filter status
        if (courseId == -1 && keyword.equals("")) {
            return repository.findQuestionBankByStatus_id(statusId, pageable);
        }
        //filter course
        if (statusId == -1  && keyword.equals("")) {
            return repository.findQuestionBankByCourse_id(courseId, pageable);
        }
        //filter course, status
        if(keyword.equals("")) {
            return repository.findQuestionBankByStatus_idAndCourse_id(statusId, courseId, pageable);
        }
        return repository.findAll(pageable);
    }

    public QuestionBank getQuestionById(Integer id) {
        return repository.getById(id);
    }
    public void saveQuestion(QuestionBank questionBank) {
        repository.saveAndFlush(questionBank);
    }

    public Set<QuestionBank> getAllQuestionsOfQuizId(Integer quizId) {
        return quizRepository.getById(quizId).getQuestionBankList();
    }
}
