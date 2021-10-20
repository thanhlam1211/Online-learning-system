package com.example.onlinelearning.repository;

import com.example.onlinelearning.entity.UserCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Admin
 */
public interface UserCourseRepository extends JpaRepository<UserCourse, Integer> {
    public List<UserCourse> findAllByUser_id(Integer id);
}