package com.example.onlinelearning.controller;

import com.example.onlinelearning.entity.Slide;
import com.example.onlinelearning.entity.Status;
import com.example.onlinelearning.repository.StatusRepository;
import com.example.onlinelearning.service.SlideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class SlideController {
    @Autowired
    private SlideService slideService;
    @Autowired
    private StatusRepository statusRepository;

    @GetMapping("/slide")
    public String slideList(Model model) {
        List<Slide> slideList = slideService.getAllSlide();
        List<Status> statusList = statusRepository.findAll();
        Slide slide = new Slide();

        model.addAttribute("statusList", statusList);
        model.addAttribute("slide", slide);
        model.addAttribute("slideList", slideList);
        return "slide";
    }
}
