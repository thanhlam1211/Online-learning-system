package com.example.onlinelearning.repository;

import com.example.onlinelearning.entity.CountCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DashBoardRepository extends JpaRepository<CountCourse, Integer> {
//Trung Đức code chỗ này
    // Query để lấy được số khoá học theo category
    @Query(value = "SELECT t1.id, ISNULL((t2.cat_count), 0) as cat_count, t1.[value] as cat_name FROM category t1 LEFT OUTER JOIN (SELECT category_id, COUNT(category_id) AS cat_count FROM course GROUP BY category_id) t2 ON t1.id = t2.category_id", nativeQuery = true)
    List<CountCourse> countCourseByCategory();

}
