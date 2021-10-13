package com.example.onlinelearning.service;

import com.example.onlinelearning.entity.Lesson;
import com.example.onlinelearning.entity.Quiz;
import com.example.onlinelearning.entity.Topic;
import com.example.onlinelearning.repository.LessonRepository;
import com.example.onlinelearning.repository.QuizRepository;
import com.example.onlinelearning.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public Page<Lesson> findAll(int currentPage, String keyword){
        Pageable pageable = PageRequest.of(currentPage - 1,6);
        if(keyword !=null){
            return lessonRepository.findAll(pageable);
        }
        return lessonRepository.findAll(pageable);
    }

    public Page<Lesson> filterLesson(String searchInput, Integer topicId, Integer statusId, int currentPage){
        Pageable pageable = PageRequest.of(currentPage - 1, 5);
        if (topicId == -1 && statusId == -1) {
            return lessonRepository.findLessonByLessonNameContaining(searchInput, pageable);
        } else if (topicId == -1) {
            return lessonRepository.findLessonByLessonNameContainingAndStatus_Id(searchInput, statusId, pageable);
        } else if (statusId == -1 ) {
            return lessonRepository.findLessonByLessonNameContainingAndTopic_TopicId(searchInput, topicId, pageable);
        } else {
            return lessonRepository.findLessonByLessonNameContainingAndStatus_IdAndTopic_TopicId(searchInput, topicId, statusId, pageable);
        }
    }
}
