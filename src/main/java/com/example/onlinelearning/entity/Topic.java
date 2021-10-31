package com.example.onlinelearning.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Admin
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "topics")
public class Topic implements Serializable {
    public Set<Lesson> getLessonList() {
        return lessonList;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer topicId;

    @Column
    private String topicName;
  
    @Column(name = "topic_order")
    private int order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL)
    private List<Lesson> lessonList;


    public List<Lesson> getLessonList(){
        Collections.sort(lessonList, new Comparator<Lesson>() {
            @Override
            public int compare(Lesson o1, Lesson o2) {
                return o1.getOrder() > o2.getOrder() ? 1 : -1;
            }
        });
        return lessonList;
    }
}
