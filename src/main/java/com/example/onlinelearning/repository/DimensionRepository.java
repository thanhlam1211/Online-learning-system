package com.example.onlinelearning.repository;


import com.example.onlinelearning.entity.Dimension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface DimensionRepository extends JpaRepository<Dimension, Integer> {
    @Query(value = "select * from dimension d\n" +
            "where\n" +
            "d.id in (select dimension_id from course_dimension where course_id = 1)", nativeQuery = true)
    public List<Dimension> findDimensionsByCourseID(Integer course_id);

    @Modifying
    @Transactional
    @Query(value = "insert into course_dimension values (?1, ?2)", nativeQuery = true)
    public void addDimensionForCourse(Integer course_id, Integer dim_id);

    @Modifying
    @Transactional
    @Query(value = "delete from course_dimension where course_id = ?1 and dimension_id = ?2", nativeQuery = true)
    public void deleteDimensionByCourseIdAndDimensionId(Integer course_id, Integer dim_id);
}
