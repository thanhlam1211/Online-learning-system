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
@Table(name = "lessons")
public class Lesson implements Serializable {
    @Id
    @GeneratedValue
    private Integer lessonId;

    @Column(name = "lesson_name")
    private String lessonName;

    @Column(name = "video_link")
    private String videoLinkId;

    @Column(name = "html_content",columnDefinition = "text")
    private String htmlContent;

    @Column(name = "lesson_order")
    private int order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    private Topic topic;

}
