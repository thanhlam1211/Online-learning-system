package com.example.onlinelearning.controller;

import com.example.onlinelearning.entity.*;
import com.example.onlinelearning.repository.RoleRepository;
import com.example.onlinelearning.repository.StatusRepository;
import com.example.onlinelearning.security.MyUserDetail;
import com.example.onlinelearning.service.BlogService;
import com.example.onlinelearning.service.CategoryService;
import com.example.onlinelearning.service.SlideService;
import com.example.onlinelearning.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
public class BlogController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private SlideService slideService;
    @Autowired
    private RoleRepository roleRepository;

//    Add new blog for student, teacher (khanh)
    @GetMapping("/newBlog")
    public String newBlog(@AuthenticationPrincipal MyUserDetail myUserDetail, Blog blog, Model model) {
        User user = myUserDetail.getUser();
        Status status = statusRepository.findByValue("INACTIVE");
        List<Slide> slideList = slideService.getAllEnabledSlides();
        List<Category> categoryList = categoryService.getAll();

        model.addAttribute("currUser", user);
        model.addAttribute("status", status);
        model.addAttribute("slideList", slideList);
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("blog", blog);

        return "newBlog";
    }

//    Save new blog for student, teacher with default status is inactive (khanh)
    @PostMapping("/addBlogNorm")
    public String saveBlogNorm(@ModelAttribute(name = "blog") Blog blog,
                           @RequestParam("fileImage") MultipartFile multipartFile) throws IOException {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        blog.setThumbnail(fileName);
        Blog savedBlog = blogService.save(blog);

        String uploadDir = "./src/main/resources/static/blog-images/" + savedBlog.getId();

        Path uploadPath = Paths.get(uploadDir);

        if(!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath , StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOException("Could not save uploaded file: " + fileName);
        }

        return "redirect:/blog/category/" + blog.getCategory().getId();
    }

//    View all blog for admin (khanh)
    @GetMapping("/admin_blog")
    public String viewAdminBlog(@AuthenticationPrincipal MyUserDetail myUserDetail, Model model, String keyword) {
        User user = myUserDetail.getUser();
        List<Blog> blogList = blogService.findAll();
        List<Status> statusList = statusRepository.findAll();
        List<Category> categoryList = categoryService.getAll();
        List<User> users = userService.getAllUsers();
        Blog blog = new Blog();

        model.addAttribute("currUser", user);
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("blog", blog);
        model.addAttribute("statusList", statusList);
        model.addAttribute("users", users);

        if (keyword != null) {
            model.addAttribute("blogList", blogService.findByKeyword(keyword));
        } else {
            model.addAttribute("blogList", blogList);
        }

        return "Admin_blog";
    }

//    Save new blog for admin (khanh)
    @PostMapping("/addBlog")
    public String saveBlog(@ModelAttribute(name = "blog") Blog blog,
                            @RequestParam("fileImage") MultipartFile multipartFile) throws IOException {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        blog.setThumbnail(fileName);
        Blog savedBlog = blogService.save(blog);

        String uploadDir = "./src/main/resources/static/blog-images/" + savedBlog.getId();

        Path uploadPath = Paths.get(uploadDir);

        if(!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath , StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOException("Could not save uploaded file: " + fileName);
        }

        return "redirect:/admin_blog";
    }

//    View blog detail for admin (khanh)
    @GetMapping("/blog/{id}")
    public String viewBlogDetail(@PathVariable(name = "id") Integer id, Model model) {
        Blog blog = blogService.getBlogById(id);
        List<Category> categoryList = categoryService.getAll();
        model.addAttribute("blog", blog);
        model.addAttribute("categoryList", categoryList);
        return "blogDetail";
    }

//    View blog edit form for admin (khanh)
    @GetMapping("/blog/details/{id}")
    public String viewBlogEditForm(@PathVariable("id") Integer id,
                                   @AuthenticationPrincipal MyUserDetail myUserDetail,
                                   Model model) {
        Role role = roleRepository.findByName("STUDENT");
        User user = myUserDetail.getUser();
        Blog blog = blogService.getBlogById(id);
        List<Category> categoryList = categoryService.getAll();
        List<Status> statusList = statusRepository.findAll();

        model.addAttribute("role", role);
        model.addAttribute("currUser", user);
        model.addAttribute("statusList", statusList);
        model.addAttribute("blog", blog);
        model.addAttribute("categoryList", categoryList);
        return "Admin_blog_edit";
    }

//    Save edited blog for admin (khanh)
    @PostMapping("/blog/update/{id}")
    public String updateBlog(@PathVariable("id") Integer id,
                              @RequestParam("fileImage") MultipartFile multipartFile) throws IOException {
        Blog blog = blogService.getBlogById(id);

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        blog.setThumbnail(fileName);

        String uploadDir = "./src/main/resources/static/blog-images/" + blog.getId();

        Path uploadPath = Paths.get(uploadDir);

        if(!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath ,StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOException("Could not save uploaded file: " + fileName);
        }

        blogService.save(blog);
        return "redirect:/admin_blog";
    }

//    Return blogs which filtered by user for admin (khanh)
    @GetMapping("/blog/filter/{id}")
    public String blogFilter(@PathVariable("id") int id,
                             @AuthenticationPrincipal MyUserDetail myUserDetail,
                             String keyword,
                             Model model){
        List<Blog> blogs = blogService.findByAuthor(id);
        User user = myUserDetail.getUser();
        List<Status> statusList = statusRepository.findAll();
        List<Category> categoryList = categoryService.getAll();
        List<User> users = userService.getAllUsers();
        Blog blog = new Blog();

        model.addAttribute("currUser", user);
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("blog", blog);
        model.addAttribute("statusList", statusList);
        model.addAttribute("users", users);

        if (keyword != null) {
            model.addAttribute("blogList", blogService.findByKeyword(keyword));
        } else {
            model.addAttribute("blogList", blogs);
        }
        return "Admin_blog";
    }

    // VIEW ALL BLOGS
    @GetMapping("blog/all")
    public String viewAllBlogsHome(Model model) {
        return viewAllBlogs(model, 1);
    }

    // VIEW ALL BLOGS PAGINATION
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

    // VIEW BLOG(s) IN A CATEGORY
    @GetMapping("/blog/category/{categoryId}")
    public String viewAllBlogsInCategoryHome(Model model, @PathVariable(name = "categoryId") Integer categoryId) {
        return viewAllBlogsInCategory(model, categoryId, 1);
    }

    // VIEW BLOG(s) IN A CATEGORY PAGINATION
    @GetMapping("/blog/category/{categoryId}/{pageNumber}")
    public String viewAllBlogsInCategory(Model model, @PathVariable(name = "categoryId") Integer categoryId, @PathVariable(name = "pageNumber") int currentPage) {
//        Page<Blog> page = blogService.getAllBlogsInCategory(categoryId, currentPage);
        Page<Blog> page1 = blogService.getAllBlogsInCategoryAndActive(categoryId, currentPage);
        long totalItems = page1.getTotalElements();
        int totalPages = page1.getTotalPages();
        List<Blog> blogList = page1.getContent();

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

    // VIEW BLOG(s) SEARCH RESULTS
    @GetMapping("/blog/search")
    public String viewAllBlogsSearchHome(Model model, @Param("query") String query) {
        return viewAllBlogsSearch(model, query, 1);

    }

    // VIEW BLOG(s) SEARCH RESULTS PAGINATION
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
