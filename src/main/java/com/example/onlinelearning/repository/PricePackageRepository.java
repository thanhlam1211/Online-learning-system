package com.example.onlinelearning.repository;

import com.example.onlinelearning.entity.PricePackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PricePackageRepository extends JpaRepository<PricePackage,Integer> {
}
