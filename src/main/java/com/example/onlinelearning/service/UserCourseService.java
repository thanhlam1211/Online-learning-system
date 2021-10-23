package com.example.onlinelearning.service;

import com.example.onlinelearning.entity.Course;
import com.example.onlinelearning.entity.User;
import com.example.onlinelearning.entity.UserCourse;
import com.example.onlinelearning.repository.UserCourseRepository;
import com.example.onlinelearning.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Admin
 */
@Service
public class UserCourseService {
    @Autowired
    private UserCourseRepository userCourseRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CourseService courseService;

    public List<UserCourse> getListCourse() {
        return userCourseRepository.findAll();
    }
    public List<UserCourse> getListCourseByUserId(Integer id) {
        return userCourseRepository.findAllByUser_id(id);
    }

    public Page<UserCourse> listAll (int pageNumber, String keyword, Integer courseId) throws NullPointerException{
        Pageable pageable = PageRequest.of(pageNumber - 1, 5);
        User user = userService.getUserByEmail(keyword);
        //filter keyword by email
        if (user != null) {
            if(user.getId() != null && courseId == -1) {
                return userCourseRepository.findUserCourseByUser_id(user.getId(), pageable);
            }
        }
        //filter keyword by course title
        Course course = courseService.getCourseByTitle(keyword);
        if (course != null) {
            if(course.getId() != null && courseId == -1) {
                return userCourseRepository.findUserCourseByCourse_id(course.getId(), pageable);
            }
        }
        //filter course
        if(keyword.equals("") && courseId != -1) {
            return userCourseRepository.findUserCourseByCourse_id(courseId, pageable);
        }
        return userCourseRepository.findAll(pageable);
    }
}