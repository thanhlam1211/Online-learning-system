package com.example.onlinelearning.repository;

import com.example.onlinelearning.entity.PricePackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface PricePackageRepository extends JpaRepository<PricePackage,Integer> {

    @Modifying
    @Transactional
    @Query(value = "update price_package set discount = ?1, duraion =?2, list_price = ?3, name = ?4, sale_price = ?5, text = ?6, status_id = ?7 where id = ?8", nativeQuery = true)
    public void updatePricePackage(
            String discount,
            Integer duraion,
            Float list_price,
            String name,
            Float sale_price,
            String text,
            Integer status_id,
            Integer package_id
    );
}
