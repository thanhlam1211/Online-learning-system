package com.example.onlinelearning.controller;

import com.example.onlinelearning.entity.*;
import com.example.onlinelearning.repository.CourseRepository;
import com.example.onlinelearning.repository.StatusRepository;
import com.example.onlinelearning.security.MyUserDetail;
import com.example.onlinelearning.service.CategoryService;
import com.example.onlinelearning.service.CourseService;
import com.example.onlinelearning.service.DimensionService;
import com.example.onlinelearning.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class CourseController {
    @Autowired
    private CourseService courseService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private DimensionService dimensionService;

    @RequestMapping("/course")
    public String viewCourse(Model model) {
        String keyword = null;
        return listByPages(model, 1, keyword);
    }

    @RequestMapping("/page/{pageNumber}")
    public String listByPages(Model model,
                              @PathVariable(name = "pageNumber") int currentPage,
                              @Param("keyword") String keyword) {
        Page<Course> page = courseService.listAll(currentPage, keyword);
        long totalItems = page.getTotalElements();
        int totalPages = page.getTotalPages();
        List<Course> listCourse = page.getContent();
        model.addAttribute("listCategory", categoryService.findAll());
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("listCourse", listCourse);
        return "course";
    }

    @GetMapping("/course_detail/{id}")
    public ModelAndView viewCourseDetail(@PathVariable(name = "id") Integer id) {
        ModelAndView modelAndView = new ModelAndView("course_detail");
        Course course = courseService.getCourseById(id);
        modelAndView.addObject("newCourses", courseService.listAll(1, null));
        modelAndView.addObject("listCategory", categoryService.findAll());
        modelAndView.addObject("course", course);
        return modelAndView;
    }

    @GetMapping("/course_modal/{id}")
    public ModelAndView viewCoursemodal(@PathVariable(name = "id") Integer id) {
        ModelAndView modelAndView = new ModelAndView("course_detail_modal");
        Course course = courseService.getCourseById(id);
        modelAndView.addObject("listCategory", categoryService.findAll());
        modelAndView.addObject("course", course);
        return modelAndView;
    }

    @GetMapping("/lesson_view/{id}")
    public String viewLesson(@PathVariable(name = "id") Integer id, Model model) {
        // Thao tác để lấy thông tin về lesson và up lên course
        return "lesson_view";
    }

    @GetMapping("/test_course_content")
    public String testCourseContentBranch() {
        return "test_course_content";
    }

    @GetMapping("/addnew_course")
    public String addCourseModal(Model model) {
        List<Category> listCate = categoryService.findAll();
        List<Status> listStatus = statusRepository.findAll();
        Course course = new Course();

        model.addAttribute("new_course", course);
        model.addAttribute("listCate", listCate);
        model.addAttribute("listStatus", listStatus);

        return "addnew_course_modal";
    }

    @PostMapping("/addnew_course")
    public String addNewCourse(@AuthenticationPrincipal MyUserDetail userDetail,
                               Model model, Course course) {
        User user = userDetail.getUser();
        courseService.saveCourseToDB(course, user);
        return "test_course_content";
    }

    @PostMapping("/course/save")
    public String saveCourse(Course course){
        courseService.save(course);
        // sau khi edit hoặc add sẽ trở về trang chính của Dương
        return "redirect:/LinkCuaDuong";
    }

    // Khi bấm nút subject detail thì sẽ link sang đây (từ html sang subject detail thì phải gửi 1 id để view)
    // getMapping cần có thêm id. Cái này chính là showEditForm, Có thể sửa thành

//    @GetMapping("/subject_detail/{id}")
//    public String viewSubjectDetail (@PathVariable("id") Integer id, Model model){

//        return "test_layout";
//    }

    //test_layout
    @GetMapping("/subject_detail")
    public String viewSubjectDetail(Model model) {
        List<Status> listStatus = statusRepository.findAll();
        List<Category> listCate = categoryService.findAll();

        Course course = new Course();
        course.setFeatured(1);
        model.addAttribute("listCate", listCate);
        model.addAttribute("listStatus", listStatus);
        model.addAttribute("dimensionList", dimensionService.getAllDimension());
        model.addAttribute("nCourse", course);
        return "test_layout";
    }
}
