package com.example.onlinelearning.repository;


import com.example.onlinelearning.entity.Dimension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DimensionRepository extends JpaRepository<Dimension, Integer> {

    @Query(value = "select * from dimension d\n" +
            "where\n" +
            "d.id in (select dimension_id from course_dimension where course_id = 1)", nativeQuery = true)
    public List<Dimension> findDimensionsByCourseID(Integer course_id);
}
