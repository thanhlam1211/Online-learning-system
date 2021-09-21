package com.example.onlinelearning.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
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
@Table(name = "status")
public class Status implements Serializable {
    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private String value;

    @OneToMany(mappedBy = "status", cascade = CascadeType.ALL)
    private Set<User> userList = new HashSet<>();

    @OneToMany(mappedBy = "status", cascade = CascadeType.ALL)
    private Set<Blog> blogList = new HashSet<>();

    @OneToMany(mappedBy = "status", cascade = CascadeType.ALL)
    private Set<Course> courseList = new HashSet<>();

    @OneToMany(mappedBy = "status", cascade = CascadeType.ALL)
    private Set<Slide> slideList = new HashSet<>();

    @OneToMany(mappedBy = "status", cascade = CascadeType.ALL)
    private Set<PricePackage> pricePackageList = new HashSet<>();

    @OneToMany(mappedBy = "status", cascade = CascadeType.ALL)
    private Set<Lesson> lessonList = new HashSet<>();

    @OneToMany(mappedBy = "status", cascade = CascadeType.ALL)
    private Set<QuestionBank> questionBankList = new HashSet<>();
}
