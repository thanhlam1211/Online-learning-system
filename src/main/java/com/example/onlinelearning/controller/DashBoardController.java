package com.example.onlinelearning.controller;

import com.example.onlinelearning.service.CourseService;
import com.example.onlinelearning.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashBoardController {

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;



}
