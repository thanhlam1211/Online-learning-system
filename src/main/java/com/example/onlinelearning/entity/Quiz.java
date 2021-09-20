package com.example.onlinelearning.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Admin
 */
@Data
@Entity
@Table(name = "quiz")
public class Quiz implements Serializable {
    @Id
    @GeneratedValue
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
}
