package com.example.onlinelearning.repository;

import com.example.onlinelearning.entity.User;
import com.example.onlinelearning.entity.UserCourse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Admin
 */
public interface UserCourseRepository extends JpaRepository<UserCourse, Integer> {
    public List<UserCourse> findAllByUser_id(Integer id);
    public Page<UserCourse> findUserCourseByUser_id(Integer userId, Pageable pageable);
    public Page<UserCourse> findUserCourseByCourse_id(Integer courseId, Pageable pageable);
    public Page<UserCourse> findUserCourseByUser_idAndCourse_id(Integer userId, Integer courseId, Pageable pageable);
}