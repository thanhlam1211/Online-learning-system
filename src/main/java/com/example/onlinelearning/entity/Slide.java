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
@Table(name = "slide")
public class Slide implements Serializable {
    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private String title;

    @Column
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "course_link")
    private String courseLink;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private Status status;
}
