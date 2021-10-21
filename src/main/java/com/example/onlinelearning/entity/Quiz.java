package com.example.onlinelearning.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Admin
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "quiz")
public class Quiz implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column(name = "number_question")
    private int numberQuestion;

    @Column
    private int duration;

    @Column(name = "pass_rate")
    private float passRate;

    @Column
    private String description;

    @OneToMany(mappedBy = "quiz", cascade =  CascadeType.ALL)
    private Set<UserQuiz> userQuizList = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "quiz_question",
            joinColumns = @JoinColumn(name = "quiz_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id"))
    private Set<QuestionBank> questionBankList = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_level_id")
    private QuizLevel quizLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_type_id")
    private QuizType quizType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;
}
