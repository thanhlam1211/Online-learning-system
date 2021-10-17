package com.example.onlinelearning.repository;

import com.example.onlinelearning.entity.DimensionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DimensionTypeRepository extends JpaRepository<DimensionType, Integer> {
}