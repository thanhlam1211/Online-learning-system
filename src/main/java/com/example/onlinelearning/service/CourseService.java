package com.example.onlinelearning.service;

import com.example.onlinelearning.entity.Category;
import com.example.onlinelearning.entity.Course;
import com.example.onlinelearning.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CategoryService categoryService;

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
}
