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
        // Initialize Linked HashMap
        LinkedHashMap<Category, List<Course>> allCategoryFeaturedCourse = new LinkedHashMap<>();

        // Get all categories
        int max_category_index = 8;  // Set maximum categories to get
        List<Category> categoryList = categoryService.getAll();
        if (categoryList.size() < 8) max_category_index = categoryList.size();
        categoryList = categoryList.subList(0, max_category_index);

        // For each category, get all courses
        for (Category category : categoryList) {
            // Get all course of this category
            List<Course> currentCourseList = getFeaturedCourseInCategory(category.getValue());
            int max_index = 8;  // Maximum courses to get
            if (currentCourseList.size() < 8) {
                max_index = currentCourseList.size();
            }

            allCategoryFeaturedCourse.put(category, currentCourseList.subList(0, max_index));
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

    // Trung Đức làm phần này
    // lưu course vào db theo từng owner của nó
    // Nhận về là course muốn thêm và user hiện tại đang log
    // lưu vào db theo kiểu many to many
    public void saveCourseToDB(Course course, User user) {
        course.setFeatured(1);
        Date currentDate  = new Date();
        course.setCreatedDate(currentDate);
        courseRepository.save(course);

        // Lưu course vào user
        courseRepository.addCourseOwner(course.getId(), user.getId());
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

    public Course getCourseByTitle(String title) {
        if(!title.equals("")){
            return courseRepository.getCourseByTitleContaining(title);
        }
        return null;
    }

    // Lấy tất cả các price package của khoá đó để cho người dùng chọn

}
