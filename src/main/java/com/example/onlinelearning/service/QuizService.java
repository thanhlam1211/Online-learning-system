package com.example.onlinelearning.service;

import com.example.onlinelearning.entity.Course;
import com.example.onlinelearning.entity.Quiz;
import com.example.onlinelearning.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class QuizService {
    @Autowired
    private QuizRepository quizRepository;

    public Page<Quiz> listAll(int currentPage, String searchInput, Integer typeId, Integer levelId) {
        Pageable pageable = PageRequest.of(currentPage - 1, 6);
        if (typeId == -1) {
            if (levelId == -1) {
                if (searchInput == "") {
                    return quizRepository.findAll(pageable);
                } else {
                    return quizRepository.findQuizzesByNameContaining(searchInput, pageable);
                }
            } else {
                if (searchInput == "") {
                    return quizRepository.findQuizzesByQuizLevelId(levelId, pageable);
                } else {
                    return quizRepository.findQuizzesByNameContainingAndQuizLevelId(searchInput, levelId, pageable);
                }
            }
        } else {
            if (levelId == -1) {
                if (searchInput == "") {
                    return quizRepository.findQuizzesByQuizTypeId(typeId, pageable);
                } else {
                    return quizRepository.findQuizzesByNameContainingAndQuizTypeId(searchInput, typeId, pageable);
                }
            } else {
                if (searchInput == "") {
                    return quizRepository.findQuizzesByQuizLevelIdAndQuizTypeId(levelId, typeId, pageable);
                } else {
                    return quizRepository.findQuizzesByNameContainingAndQuizLevelIdAndQuizTypeId(searchInput, levelId, typeId, pageable);
                }
            }
        }
    }

    public Quiz getQuizById(int id) {
        return quizRepository.getById(id);
    }

    public void saveQuiz(Quiz quiz) {
        quizRepository.save(quiz);
    }

}
