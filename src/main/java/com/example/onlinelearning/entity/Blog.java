package com.example.onlinelearning.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Admin
 */
@Data
@Entity
@Table(name = "blog")
public class Blog implements Serializable {
    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private String thumbnail;

    @Column
    private String brief;

    @Column
    private int featured;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}


