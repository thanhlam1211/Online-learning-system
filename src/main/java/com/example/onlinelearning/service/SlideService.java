package com.example.onlinelearning.service;

import com.example.onlinelearning.entity.Slide;
import com.example.onlinelearning.repository.SlideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SlideService {
    @Autowired
    private SlideRepository slideRepository;

    public List<Slide> getAllEnabledSlides() {
        return slideRepository.findAllByStatusValue("ACTIVE");
    }
    public List<Slide> getAllSlide() {
        return slideRepository.findAll();
    }
}
