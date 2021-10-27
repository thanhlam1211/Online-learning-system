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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinColumn(name = "status_id")
    private Status status;


    @ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinColumn(name = "levelId")
    private QuizLevel quizLevel;

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(name = "question_dimension",
            joinColumns =@JoinColumn(name = "question_id"),
            inverseJoinColumns =@JoinColumn(name = "dimension_id"))
    private Set<Dimension> dimensionList = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToMany(mappedBy = "questionBank", cascade = CascadeType.ALL)
    private Set<UserQuestionAnswer> userQuestionAnswerList = new HashSet<>();

    public String getUserQuestionAnswer_userChoice(int user_quiz_id){
        String answer = "";
        for (UserQuestionAnswer userQuestionAnswer: userQuestionAnswerList) {
            if(userQuestionAnswer.getUserQuiz().getId()==user_quiz_id){
                answer = userQuestionAnswer.getUserChoice();
            }
        }
        return answer;
    }

    @ManyToMany(mappedBy = "questionBankList")
    private Set<Quiz> quizList = new HashSet<>();


}
