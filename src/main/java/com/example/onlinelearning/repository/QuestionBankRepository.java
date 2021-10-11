package com.example.onlinelearning.repository;

import com.example.onlinelearning.entity.QuestionBank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Admin
 */
@Repository
public interface QuestionBankRepository extends JpaRepository<QuestionBank, Integer> {
    public Page<QuestionBank> findByContentContaining(String content, Pageable pageable);
    public QuestionBank getById(Integer id);
}
