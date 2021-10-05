package com.example.onlinelearning.controller;

import com.example.onlinelearning.entity.Blog;
import com.example.onlinelearning.entity.Category;
import com.example.onlinelearning.entity.Course;
import com.example.onlinelearning.entity.Slide;
import com.example.onlinelearning.repository.SlideRepository;
import com.example.onlinelearning.service.BlogService;
import com.example.onlinelearning.service.CategoryService;
import com.example.onlinelearning.service.CourseService;
import com.example.onlinelearning.service.SlideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;

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
        List<Category> categoryList = categoryService.getAll();
        List<Slide> slideList = slideService.getAllEnabledSlides();
        HashMap<Category, List<Course>> allCategoryFeaturedCourse = courseService.getAllCategoryFeaturedCourse();
        List<Blog> featuredBlogList = blogService.getFeaturedBlogs();
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("slideList", slideList);
        model.addAttribute("allCategoryFeaturedCourse", allCategoryFeaturedCourse);
        model.addAttribute("featuredBlogList", featuredBlogList);
        return "index";
    }
}
