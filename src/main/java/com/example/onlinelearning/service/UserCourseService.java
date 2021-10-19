package com.example.onlinelearning.service;

import com.example.onlinelearning.entity.UserCourse;
import com.example.onlinelearning.repository.UserCourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Admin
 */
@Service
public class UserCourseService {
    @Autowired
    private UserCourseRepository repository;

    public List<UserCourse> getListCourse() {
        return repository.findAll();
    }

}