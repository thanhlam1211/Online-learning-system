package com.example.onlinelearning.service;

import com.example.onlinelearning.entity.Course;
import com.example.onlinelearning.entity.Lesson;
import com.example.onlinelearning.entity.Quiz;
import com.example.onlinelearning.entity.Topic;
import com.example.onlinelearning.repository.CourseRepository;
import com.example.onlinelearning.repository.LessonRepository;
import com.example.onlinelearning.repository.QuizRepository;
import com.example.onlinelearning.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

@Service
public class LessonService {
    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private CourseRepository courseRepository;

    public List<Lesson> findLessonsByTopic_id(Integer id){
        return lessonRepository.findByTopic_TopicIdOrderByOrder(id);
    }

    public int getNumberOfLessonsInCourseId(Integer courseId) {
        Course currentCourse = courseRepository.getById(courseId);
        return currentCourse.getLessonList().size();

    }

    public Page<Lesson> findAll(int currentPage, String keyword){
        Pageable pageable = PageRequest.of(currentPage - 1,6);
        if(keyword !=null){
            return lessonRepository.findAll(pageable);
        }
        return lessonRepository.findAll(pageable);
    }

    // Trung Đức làm phần này
    // Filter theo từng field
    public Page<Lesson> filterLesson(Integer courseId, String searchInput, Integer topicId, Integer statusId, int currentPage){
        Pageable pageable = PageRequest.of(currentPage - 1, 5);
        if (topicId == -1 && statusId == -1) {
            return lessonRepository.findLessonByCourse_IdAndLessonNameContaining(courseId,searchInput, pageable);
        } else if (topicId == -1) {
            return lessonRepository.findLessonByCourse_IdAndLessonNameContainingAndStatus_Id(courseId,searchInput, statusId, pageable);
        } else if (statusId == -1 ) {
            return lessonRepository.findLessonByCourse_IdAndLessonNameContainingAndTopic_TopicId(courseId,searchInput, topicId, pageable);
        } else {
            return lessonRepository.findLessonByCourse_IdAndLessonNameContainingAndStatus_IdAndTopic_TopicId(courseId,searchInput, topicId, statusId, pageable);
        }
    }

    public LinkedHashMap<Topic, List<Lesson>> getCourseContentByCourseId(Integer courseId) {
        LinkedHashMap<Topic, List<Lesson>> courseContent = new LinkedHashMap<>();

        // Get all topics order by Order
        List<Topic> topicList = topicRepository.findAllByCourse_IdOrderByOrder(courseId);

        // Get all lessons for each topic
        for (Topic topic : topicList) {
            courseContent.put(topic, lessonRepository.findByTopic_TopicIdOrderByOrder(topic.getTopicId()));
        }

        return courseContent;

    }

    public Integer getFirstLessonId(Integer courseId) {
        // Get all topics order by Order
        List<Topic> topicList = topicRepository.findAllByCourse_IdOrderByOrder(courseId);

        // Return first LessonId
        for (Topic topic : topicList) {
            List<Lesson> currentLessonList = lessonRepository.findByTopic_TopicIdOrderByOrder(topic.getTopicId());
            if (!currentLessonList.isEmpty()) return currentLessonList.get(0).getLessonId();
        }

        return -1;
    }

    public Integer getNextLessonId(Integer currentLessonId, LinkedHashMap<Topic, List<Lesson>> courseContent) {
        boolean flag = false;
        for (Topic topic : courseContent.keySet()) {
            for (Lesson lesson : courseContent.get(topic)) {
                if (flag) return lesson.getLessonId();
                if (lesson.getLessonId() == currentLessonId) flag = true;

            }
        }
        return -1;
    }

    public Integer getPreviousLessonId(Integer currentLessonId, LinkedHashMap<Topic, List<Lesson>> courseContent) {
        Lesson temp = new Lesson();
        temp.setLessonId(-1);
        for (Topic topic : courseContent.keySet()) {
            for (Lesson lesson : courseContent.get(topic)) {
                if (lesson.getLessonId() != currentLessonId) {
                    temp = lesson;
                } else {
                    return temp.getLessonId();
                }

            }
        }
        return -1;
    }
}
