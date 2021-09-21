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
@Table(name = "category")
public class Category implements Serializable {
    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private String value;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private Set<Blog> blogList = new HashSet<>();

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private Set<Course> courseList = new HashSet<>();


}
