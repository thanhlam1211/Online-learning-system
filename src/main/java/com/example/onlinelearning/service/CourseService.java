package com.example.onlinelearning.service;

import com.example.onlinelearning.entity.*;
import com.example.onlinelearning.repository.CourseRepository;
import com.example.onlinelearning.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserRepository userRepository;

    public List<Course> getFeaturedCourseInCategory(String category) {
        return courseRepository.findCourseByCategory_ValueAndFeaturedEqualsAndStatus_Value(category, 1, "ACTIVE");
    }

    public LinkedHashMap<Category, List<Course>> getAllCategoryFeaturedCourse() {
        LinkedHashMap<Category, List<Course>> allCategoryFeaturedCourse = new LinkedHashMap<>();
        List<Category> categoryList = categoryService.getAll();
        for (Category category : categoryList) {
            allCategoryFeaturedCourse.put(category, getFeaturedCourseInCategory(category.getValue()));
        }
        return allCategoryFeaturedCourse;
    }
    public Course getCourseById(int id){
        return courseRepository.getById(id);
    }

    public Page<Course> listAll(int currentPage, String keyword){
        Pageable pageable = PageRequest.of(currentPage - 1,6);
        if(keyword !=null){
            return courseRepository.findByTitleContainingOrCategoryValueOrderByIdDesc(keyword,keyword,pageable);
        }

        return courseRepository.findAllByOrderByIdDesc(pageable);
    }

    public void saveCourseToDB(Course course, User user) {
        Set<User> userList = new HashSet<>();
        userList.add(user);
        course.setUserList(userList);
        course.setFeatured(1);
        course.addUser(user);
        Date currentDate  = new Date();
        course.setCreatedDate(currentDate);
        //course.addUser(user);
        user.addCourse(course);
//        userRepository.save(user);
        courseRepository.save(course);
    }

    // get course to edit
    public Course getCourse(int id){
        Optional<Course> result = courseRepository.findById(id);
        return result.get();
    }
    public void save(Course course){
        courseRepository.save(course);
    }
}
