package com.example.onlinelearning.service;

import com.example.onlinelearning.entity.Dimension;
import com.example.onlinelearning.repository.DimensionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
