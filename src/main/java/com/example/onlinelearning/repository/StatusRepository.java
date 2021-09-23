package com.example.onlinelearning.repository;

import com.example.onlinelearning.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Admin
 */
public interface StatusRepository extends JpaRepository<Status, Integer> {
    public Status findByValue(String name);
}
