package com.example.onlinelearning.service;

import com.example.onlinelearning.entity.Topic;
import com.example.onlinelearning.repository.LessonRepository;
import com.example.onlinelearning.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessonService {
    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private TopicRepository topicRepository;

    public int getNumberOfLessonsInCourseId(Integer courseId) {
        int totalLessons = 0;

        // Get all topics
        List<Topic> topicList = topicRepository.findAllByCourse_Id(courseId);

        for (Topic topic : topicList) {
            totalLessons = topic.getLessonList().size();
        }

        return totalLessons;

    }
}
