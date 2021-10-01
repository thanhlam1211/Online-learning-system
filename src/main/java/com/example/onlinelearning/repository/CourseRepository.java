package com.example.onlinelearning.repository;

import com.example.onlinelearning.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    public List<Course> findCourseByCategory_ValueAndFeaturedEqualsAndStatus_Value(String category, int featured, String status);
}
