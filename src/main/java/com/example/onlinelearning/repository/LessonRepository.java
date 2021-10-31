package com.example.onlinelearning.repository;

import com.example.onlinelearning.entity.Lesson;
import com.example.onlinelearning.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    public Page<Lesson> findLessonByLessonNameContaining(String title, Pageable pageable);

    public Page<Lesson> findLessonByLessonNameContainingAndStatus_Id(String title, Integer statusId, Pageable pageable);

    public Page<Lesson> findLessonByLessonNameContainingAndStatus_IdAndTopic_TopicId(String title, Integer statusId, Integer topicId, Pageable pageable);

    public Page<Lesson> findLessonByLessonNameContainingAndTopic_TopicId(String title, Integer topicId, Pageable pageable);

    public Page<Lesson> findLessonByCourse_IdAndLessonNameContaining(Integer courseId, String title, Pageable pageable);

    public Page<Lesson> findLessonByCourse_IdAndLessonNameContainingAndStatus_Id(Integer courseId, String title, Integer statusId, Pageable pageable);

    public Page<Lesson> findLessonByCourse_IdAndLessonNameContainingAndStatus_IdAndTopic_TopicId(Integer courseId, String title, Integer statusId, Integer topicId, Pageable pageable);

    public Page<Lesson> findLessonByCourse_IdAndLessonNameContainingAndTopic_TopicId(Integer courseId, String title, Integer topicId, Pageable pageable);

    public List<Lesson> findByTopic_TopicIdOrderByOrder(Integer topic_topicId);

}
