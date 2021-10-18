package com.example.onlinelearning.repository;

import com.example.onlinelearning.entity.Course;
import com.example.onlinelearning.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    public Page<Course> findCoursesByCategoryIdAndTitleContaining(Integer category_id, String title, Pageable pageable);
    public Page<Course> findCoursesByCategoryIdOrderByIdDesc(Integer category_id, Pageable pageable);
    public List<Course> findCourseByCategory_ValueAndFeaturedEqualsAndStatus_Value(String category, int featured, String status);
    public Page<Course> findCourseByTitleContaining(String title, Pageable pageable);
    public Page<Course> findCourseByTitleContainingAndCategory_IdAndStatus_Id(String title, Integer categoryId, Integer statusId, Pageable pageable);
    public Page<Course> findCourseByTitleContainingAndCategory_Id(String title, Integer categoryId, Pageable pageable);
    public Page<Course> findCourseByTitleContainingAndStatus_Id(String title, Integer statusId, Pageable pageable);
    public Page<Course> findCourseByTitleContainingAndUserListContains(String title, User user, Pageable pageable);
    public Page<Course> findCourseByTitleContainingAndCategory_IdAndStatus_IdAndUserListContains(String title, Integer categoryId, Integer statusId, User user, Pageable pageable);
    public Page<Course> findCourseByTitleContainingAndCategory_IdAndUserListContains(String title, Integer categoryId, User user, Pageable pageable);
    public Page<Course> findCourseByTitleContainingAndStatus_IdAndUserListContains(String title, Integer statusId, User user, Pageable pageable);


    @Modifying
    @Transactional
    @Query(value = "insert into owner values (?1, ?2)", nativeQuery = true)
    public void addCourseOwner(Integer course_id, Integer user_id);
}
