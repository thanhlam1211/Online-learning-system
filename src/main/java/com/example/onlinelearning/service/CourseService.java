package com.example.onlinelearning.service;

import com.example.onlinelearning.entity.Course;
import com.example.onlinelearning.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    public Page<Course> listAll(int currentPage, String keyword){
        Pageable pageable = PageRequest.of(currentPage - 1,3);
        if(keyword !=null){
            return courseRepository.findByTitleContainingOrCategoryValue(keyword,keyword,pageable);
        }

        return courseRepository.findAll(pageable);
    }
}
