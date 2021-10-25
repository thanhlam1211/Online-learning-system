package com.example.onlinelearning.repository;

import com.example.onlinelearning.entity.BarChart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BarChartRepository extends JpaRepository<BarChart, Integer> {

    @Query(value = "SELECT f.id, count(f.id) as count_course, f.name as tag_name FROM  course_dimension l JOIN  dimension f ON (l.dimension_id = f.id AND l.dimension_id IN (select course.id from course)) GROUP BY f.id, f.name", nativeQuery = true)
    List<BarChart> countCourseByTag();

}
