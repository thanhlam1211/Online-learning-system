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
@Table(name = "user_course")
public class UserCourse implements Serializable {
    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private Date startDate;

    @Column
    private Date endDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
}
