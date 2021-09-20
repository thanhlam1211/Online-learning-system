package com.example.onlinelearning.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Admin
 */
@Data
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;
}
