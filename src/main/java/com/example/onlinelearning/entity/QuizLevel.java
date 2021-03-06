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
@Table(name = "quiz_level")
public class QuizLevel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @OneToMany(mappedBy = "quizLevel", cascade = CascadeType.ALL)
    private Set<Quiz> quizList = new HashSet<>();

    @OneToMany(mappedBy = "quizLevel", cascade = CascadeType.ALL)
    private Set<QuestionBank> questionBankList = new HashSet<>();
}
