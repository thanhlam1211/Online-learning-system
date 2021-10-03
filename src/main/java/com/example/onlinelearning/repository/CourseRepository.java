package com.example.onlinelearning.repository;

import com.example.onlinelearning.entity.Category;
import com.example.onlinelearning.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    public Page<Course> findByTitleContainingOrCategoryValueOrderByIdDesc(String title, String categoryValue, Pageable pageable);
    public Page<Course> findAllByOrderByIdDesc(Pageable pageable);
}
