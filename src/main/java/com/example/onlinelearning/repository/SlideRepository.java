package com.example.onlinelearning.repository;

import com.example.onlinelearning.entity.Slide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface SlideRepository extends JpaRepository<Slide, Integer> {
    public List<Slide> findAllByStatusValue(String status);
    public Slide getSlideById(Integer id);
}
