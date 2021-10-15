package com.example.onlinelearning.repository;

import com.example.onlinelearning.entity.QuestionBank;
import com.example.onlinelearning.entity.QuizLevel;
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
    public Page<QuestionBank> findQuestionBankByStatus_id(Integer status, Pageable pageable);
//    public Page<QuestionBank> findByLevelId(Integer level, Pageable pageable);
    public Page<QuestionBank> findQuestionBankByCourse_id(Integer course, Pageable pageable);
    public Page<QuestionBank> findQuestionBankByStatus_idAndCourse_id(Integer status,Integer course, Pageable pageable);


}
