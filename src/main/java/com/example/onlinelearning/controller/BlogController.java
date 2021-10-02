package com.example.onlinelearning.controller;

import com.example.onlinelearning.entity.Blog;
import com.example.onlinelearning.entity.Category;
import com.example.onlinelearning.service.BlogService;
import com.example.onlinelearning.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class BlogController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/blog/{id}")
    public String viewBlogDetail(@PathVariable(name = "id") Integer id, Model model) {
        Blog blog = blogService.getBlogById(id);
        List<Category> categoryList = categoryService.getAll();
        model.addAttribute("blog", blog);
        model.addAttribute("categoryList", categoryList);
        return "blogDetail";
    }
}
