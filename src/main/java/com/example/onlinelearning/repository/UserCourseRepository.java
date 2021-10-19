package com.example.onlinelearning.repository;

import com.example.onlinelearning.entity.UserCourse;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Admin
 */
public interface UserCourseRepository extends JpaRepository<UserCourse, Integer> {
}