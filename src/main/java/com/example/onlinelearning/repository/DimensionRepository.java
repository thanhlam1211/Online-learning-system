package com.example.onlinelearning.repository;

import com.example.onlinelearning.entity.Dimension;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Admin
 */
public interface DimensionRepository extends JpaRepository<Dimension, Integer> {
}
