package com.example.onlinelearning.repository;

import com.example.onlinelearning.entity.Course;
import com.example.onlinelearning.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    public List<Course> findCourseByCategory_ValueAndFeaturedEqualsAndStatus_Value(String category, int featured, String status);
    public Page<Course> findByTitleContainingOrCategoryValueOrderByIdDesc(String title, String categoryValue, Pageable pageable);
    public Page<Course> findAllByOrderByIdDesc(Pageable pageable);
    public Page<Course> findCourseByTitleContaining(String title, Pageable pageable);
    public Page<Course> findCourseByTitleContainingAndCategory_IdAndStatus_Id(String title, Integer categoryId, Integer statusId, Pageable pageable);
    public Page<Course> findCourseByTitleContainingAndCategory_Id(String title, Integer categoryId, Pageable pageable);
    public Page<Course> findCourseByTitleContainingAndStatus_Id(String title, Integer statusId, Pageable pageable);
    public Page<Course> findCourseByTitleContainingAndUserListContains(String title, User user, Pageable pageable);
    public Page<Course> findCourseByTitleContainingAndCategory_IdAndStatus_IdAndUserListContains(String title, Integer categoryId, Integer statusId, User user, Pageable pageable);
    public Page<Course> findCourseByTitleContainingAndCategory_IdAndUserListContains(String title, Integer categoryId, User user, Pageable pageable);
    public Page<Course> findCourseByTitleContainingAndStatus_IdAndUserListContains(String title, Integer statusId, User user, Pageable pageable);
}
