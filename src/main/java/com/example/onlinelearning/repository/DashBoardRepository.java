package com.example.onlinelearning.repository;

import com.example.onlinelearning.entity.CountCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DashBoardRepository extends JpaRepository<CountCourse, Integer> {

    @Query(value = "SELECT t1.id, (t2.cat_count), t1.[value] FROM category t1 LEFT OUTER JOIN (SELECT category_id, COUNT(category_id) AS cat_count FROM course GROUP BY category_id) t2 ON t1.id = t2.category_id", nativeQuery = true)
    List<CountCourse> countCourseByCategory();

}
