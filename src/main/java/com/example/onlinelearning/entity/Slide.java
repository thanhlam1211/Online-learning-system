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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

//    Display image by path for slide
    @Transient
    public String getSlideImagePath() {
        if (imageUrl == null || id == null) return null;

        return "/slide-images/" + id + "/" + imageUrl;
    }
}
