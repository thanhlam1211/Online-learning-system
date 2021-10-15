package com.example.onlinelearning.service;


import com.example.onlinelearning.entity.DimensionType;
import com.example.onlinelearning.repository.DimensionTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DimensionTypeService {
    @Autowired
    private DimensionTypeRepository dimensionTypeRepository;

    public List<DimensionType> getAllDimensionType() {
        return dimensionTypeRepository.findAll();
    }

    public void saveType(DimensionType dimensionType) {
        dimensionTypeRepository.save(dimensionType);
    }
}
