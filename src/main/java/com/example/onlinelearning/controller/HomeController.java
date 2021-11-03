package com.example.onlinelearning.controller;

import com.example.onlinelearning.entity.*;
import com.example.onlinelearning.security.MyUserDetail;
import com.example.onlinelearning.service.BlogService;
import com.example.onlinelearning.service.CategoryService;
import com.example.onlinelearning.service.CourseService;
import com.example.onlinelearning.service.SlideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

@Controller
public class HomeController {
    @Autowired
    private SlideService slideService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private BlogService blogService;


    @GetMapping("/")
    public String viewHome(Model model) {
        // Get all categories for navbar
        List<Category> categoryList = categoryService.getAll();

        // Get all enabled slides to display on header
        List<Slide> slideList = slideService.getAllEnabledSlides();

        // Get all courseContent to display
        LinkedHashMap<Category, List<Course>> allCategoryFeaturedCourse = courseService.getAllCategoryFeaturedCourse();

        // Get featured blogs to display
        List<Blog> featuredBlogList = blogService.getFeaturedBlogs();

        // Assign to model to use on thymeleaf later on
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("slideList", slideList);
        model.addAttribute("allCategoryFeaturedCourse", allCategoryFeaturedCourse);
        model.addAttribute("featuredBlogList", featuredBlogList);
        return "index";
    }
}
