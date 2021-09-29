package com.example.onlinelearning.controller;

import com.example.onlinelearning.entity.Slide;
import com.example.onlinelearning.repository.SlideRepository;
import com.example.onlinelearning.service.SlideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private SlideService slideService;

    @Autowired
    private SlideRepository slideRepository;

    @GetMapping("/")
    public String viewHome(Model model) {
        List<Slide> slideList = new ArrayList<>();
        slideList = slideRepository.findAll();
        model.addAttribute("slideList", slideList);
        return "home";
    }
}
