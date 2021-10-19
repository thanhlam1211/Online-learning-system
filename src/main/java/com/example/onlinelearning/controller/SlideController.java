package com.example.onlinelearning.controller;

import com.example.onlinelearning.service.SlideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SlideController {
    @Autowired
    private SlideService slideService;

    @GetMapping("/slide")
    public String slideList() {
        return "slide";
    }
}
