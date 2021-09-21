package com.example.onlinelearning.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Admin
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user_quiz")
public class UserQuiz implements Serializable {
    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private float mark;

    @Column(name = "start_time")
    private Date startTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @OneToMany(mappedBy = "userQuiz", cascade = CascadeType.ALL)
    private Set<UserQuestionAnswer> userQuestionAnswerList = new HashSet<>();
}
