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

    public Page<Course> listAll(int currentPage, String searchInput, Integer categoryId){
        Pageable pageable = PageRequest.of(currentPage - 1,6);
        if(categoryId==-1){
            if(searchInput==""){
                return courseRepository.findAll(pageable);
            }
            else{
                return courseRepository.findCourseByTitleContaining(searchInput,pageable);
            }
        }
        else if (searchInput==""){
            return courseRepository.findCoursesByCategoryIdOrderByIdDesc(categoryId,pageable);
        }
        else
            return courseRepository.findCoursesByCategoryIdAndTitleContaining(categoryId,searchInput,pageable);
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
//        user.addCourse(course);
        userRepository.save(user);
        courseRepository.save(course);
    }

    public List<Course> findAll() {
        return courseRepository.findAll();
    }


    public Page<Course> filterCoursesByAdmin(String searchInput, Integer categoryId, Integer statusId, int currentPage) {
        Pageable pageable = PageRequest.of(currentPage - 1, 5);
        if (categoryId == -1 && statusId == -1) {
            return courseRepository.findCourseByTitleContaining(searchInput, pageable);
        } else if (categoryId == -1) {
            return courseRepository.findCourseByTitleContainingAndStatus_Id(searchInput, statusId, pageable);
        } else if (statusId == -1 ) {
            return courseRepository.findCourseByTitleContainingAndCategory_Id(searchInput, categoryId, pageable);
        } else {
            return courseRepository.findCourseByTitleContainingAndCategory_IdAndStatus_Id(searchInput, categoryId, statusId, pageable);
        }
    }

    public Page<Course> filterCoursesByTeacher(User user, String searchInput, Integer categoryId, Integer statusId, int currentPage) {
        Pageable pageable = PageRequest.of(currentPage - 1, 5);
        if (categoryId == -1 && statusId == -1) {
            return courseRepository.findCourseByTitleContainingAndUserListContains(searchInput, user, pageable);
        } else if (categoryId == -1) {
            return courseRepository.findCourseByTitleContainingAndStatus_IdAndUserListContains(searchInput, statusId, user, pageable);
        } else if (statusId == -1 ) {
            return courseRepository.findCourseByTitleContainingAndCategory_IdAndUserListContains(searchInput, categoryId, user, pageable);
        } else {
            return courseRepository.findCourseByTitleContainingAndCategory_IdAndStatus_IdAndUserListContains(searchInput, categoryId, statusId, user, pageable);
        }
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
