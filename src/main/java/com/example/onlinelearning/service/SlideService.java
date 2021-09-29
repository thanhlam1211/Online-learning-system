package com.example.onlinelearning.service;

import com.example.onlinelearning.repository.SlideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SlideService {
    @Autowired
    private SlideRepository slideRepository;


}
