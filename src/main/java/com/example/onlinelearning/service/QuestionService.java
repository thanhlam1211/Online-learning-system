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

    public Page<QuestionBank> listAll(int pageNumber, String keyword) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 2);
        if(keyword != null) {
            return repository.findByContentContaining(keyword, pageable);
        }
        return repository.findAll(pageable);
    }
}
