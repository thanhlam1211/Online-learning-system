package com.example.onlinelearning.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Admin
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "question_bank")
public class QuestionBank implements Serializable {
    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private String content;

    @Column
    private String explanation;

    @Column
    private String option1;

    @Column
    private String option2;

    @Column
    private String option3;

    @Column
    private String option4;

    @Column
    private String answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "levelId")
    private QuizLevel quizLevel;

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(name = "question_dimension",
            joinColumns =@JoinColumn(name = "question_id"),
            inverseJoinColumns =@JoinColumn(name = "dimension_id"))
    private Set<Dimension> dimensionList = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToMany(mappedBy = "questionBank", cascade = CascadeType.ALL)
    private Set<UserQuestionAnswer> userQuestionAnswerList = new HashSet<>();

    @ManyToMany(mappedBy = "questionBankList")
    private Set<Quiz> quizList = new HashSet<>();

}
