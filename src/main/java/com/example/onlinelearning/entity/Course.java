package com.example.onlinelearning.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Admin
 */
@Data
@Entity
@Table(name = "course")
public class Course implements Serializable {
    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private String title;

    @Column(name = "short_description")
    private String shortDescription;

    @Column
    private String description;

    @Column
    private int featured;

    @Column(name = "thumbnail_url")
    private String thumbnail;

    @Column
    @CreatedDate
    private Date createdDate;

    @ManyToMany(mappedBy = "courseList")
    private Set<User> userList = new HashSet<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private Set<UserCourse> userCourseList = new HashSet<>();
}
