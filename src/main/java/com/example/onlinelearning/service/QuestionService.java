package com.example.onlinelearning.service;

/**
 * @author Admin
 */

import com.example.onlinelearning.entity.QuestionBank;
import com.example.onlinelearning.repository.QuestionBankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {
    @Autowired
    private QuestionBankRepository repository;

    public Page<QuestionBank> listAll(int pageNumber, String keyword, Integer courseId, Integer dimensionId, Integer levelId, Integer statusId ) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 2);
        if(keyword != null) {
            return repository.findByContentContaining(keyword, pageable);
        }
        if (courseId == -1 && statusId == -1 && dimensionId == -1 && levelId == -1) {
            return repository.findAll(pageable);
        }
        //filter levelId
        if (courseId == -1 && statusId == -1 && dimensionId == -1) {
//            return repository.findQuestionBankByLevel_id(levelId, pageable);
        }
        //filter status
        if (courseId == -1 && levelId == -1 && dimensionId == -1) {
            return repository.findQuestionBankByStatus_id(statusId, pageable);
        }
        //filter course
        if (levelId == -1 && statusId == -1 && dimensionId == -1) {
            return repository.findQuestionBankByCourse_id(courseId, pageable);
        }
        //filter level and dimension
        if (statusId == -1 && courseId == -1) {
//            return repository
        }
        //filter level, courseId and dimension
        if(statusId == -1) {
//            return repository
        }
        //filter course, status, dimension
        if(levelId == -1) {

        }
        //filter course, status, level
        if(dimensionId == -1) {

        }
        //filter status, dimension, level
        if(courseId == - 1) {

        }
        //filter status, dimension
        if(courseId == -1 && levelId == -1) {

        }
        //filter dimension, courseId
        if(statusId == -1 && levelId == -1) {

        }
        //filter course, status
        if(dimensionId == -1 && levelId == -1) {
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
}
