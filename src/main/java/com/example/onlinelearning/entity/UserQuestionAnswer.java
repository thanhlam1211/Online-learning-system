package com.example.onlinelearning.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Admin
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user_question_answer")
public class UserQuestionAnswer implements Serializable {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "user_choice")
    private String userChoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_quiz_id")
    private UserQuiz userQuiz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private QuestionBank questionBank;
}
