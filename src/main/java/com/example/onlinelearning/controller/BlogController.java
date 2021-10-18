package com.example.onlinelearning.controller;

import com.example.onlinelearning.entity.Blog;
import com.example.onlinelearning.entity.Category;
import com.example.onlinelearning.repository.BlogRepository;
import com.example.onlinelearning.service.BlogService;
import com.example.onlinelearning.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
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

    @GetMapping("blog/all")
    public String viewAllBlogsHome(Model model) {
        return viewAllBlogs(model, 1);
    }

    @GetMapping("/blog/all/{pageNumber}")
    public String viewAllBlogs(Model model, @PathVariable(name = "pageNumber") int currentPage) {
        Page<Blog> page = blogService.listAll(currentPage);
        long totalItems = page.getTotalElements();
        int totalPages = page.getTotalPages();
        List<Blog> blogList = page.getContent();

        model.addAttribute("totalItems", totalItems);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("blogList", blogList);
        model.addAttribute("pageTitle", "Latest Posts");
        model.addAttribute("nextBaseURL", "/blog/all/");
        model.addAttribute("query", "");


        List<Category> categoryList = categoryService.getAll();
        model.addAttribute("categoryList", categoryList);
        return "blogList";
    }

    @GetMapping("/blog/category/{categoryId}")
    public String viewAllBlogsInCategoryHome(Model model, @PathVariable(name = "categoryId") Integer categoryId) {
        return viewAllBlogsInCategory(model, categoryId, 1);
    }

    @GetMapping("/blog/category/{categoryId}/{pageNumber}")
    public String viewAllBlogsInCategory(Model model, @PathVariable(name = "categoryId") Integer categoryId, @PathVariable(name = "pageNumber") int currentPage) {
        Page<Blog> page = blogService.getAllBlogsInCategory(categoryId, currentPage);
        long totalItems = page.getTotalElements();
        int totalPages = page.getTotalPages();
        List<Blog> blogList = page.getContent();

        model.addAttribute("totalItems", totalItems);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("blogList", blogList);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("nextBaseURL", "/blog/category/" + categoryId + "/");
        model.addAttribute("query", "");
        model.addAttribute("pageTitle", "Category: " + categoryService.getById(categoryId).getValue());


        List<Category> categoryList = categoryService.getAll();
        model.addAttribute("categoryList", categoryList);
        return "blogList";
    }

    @GetMapping("/blog/search")
    public String viewAllBlogsSearchHome(Model model, @Param("query") String query) {
        return viewAllBlogsSearch(model, query, 1);

    }

    @GetMapping("/blog/search/{pageNumber}")
    public String viewAllBlogsSearch(Model model, @Param("query") String query, @PathVariable(name = "pageNumber") int currentPage) {
        Page<Blog> page = blogService.getAllBlogsTitleContaining(query, currentPage);
        long totalItems = page.getTotalElements();
        int totalPages = page.getTotalPages();
        List<Blog> blogList = page.getContent();

        model.addAttribute("totalItems", totalItems);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("blogList", blogList);
        model.addAttribute("nextBaseURL", "/blog/search/");
        model.addAttribute("query", "/?query=" + query);
        model.addAttribute("pageTitle", "Search Results: '" + query + "'");


        List<Category> categoryList = categoryService.getAll();
        model.addAttribute("categoryList", categoryList);
        return "blogList";

    }
}
