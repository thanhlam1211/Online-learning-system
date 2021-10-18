package com.example.onlinelearning.service;

import com.example.onlinelearning.entity.Course;
import com.example.onlinelearning.entity.Dimension;
import com.example.onlinelearning.repository.DimensionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DimensionService {
    @Autowired
    private DimensionRepository dimensionRepository;

    public List<Dimension> getDimensionByCourseID(Integer course_id) {
        return dimensionRepository.findDimensionsByCourseID(course_id);
    }
    public List<Dimension> getAllDimension() {
        return dimensionRepository.findAll();
    }
    public void deleteDimension(Integer course_id, Integer dim_id) {
        dimensionRepository.deleteDimensionByCourseIdAndDimensionId(course_id, dim_id);
    }
    public void addDimensionForCourse(Integer course_id, Integer dim_id) {
        dimensionRepository.addDimensionForCourse(course_id, dim_id);
    }
}
