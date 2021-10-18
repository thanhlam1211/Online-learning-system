package com.example.onlinelearning.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Admin
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "blog")
public class Blog implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String title;

    @Column(columnDefinition = "text")
    private String content;

    @Column
    private String thumbnail;

    @Column(columnDefinition = "text")
    private String brief;

    @Column
    private int featured;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="status_id")
    private Status status;

}


