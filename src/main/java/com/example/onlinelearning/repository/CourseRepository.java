package com.example.onlinelearning.repository;

import com.example.onlinelearning.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    public Page<Course> findByTitleContainingOrCategoryValueOrderByIdDesc(String name, String brand, Pageable pageable);
}
