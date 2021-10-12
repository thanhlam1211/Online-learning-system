package com.example.onlinelearning.controller;

import com.example.onlinelearning.entity.*;
import com.example.onlinelearning.repository.CourseRepository;
import com.example.onlinelearning.repository.StatusRepository;
import com.example.onlinelearning.security.MyUserDetail;
import com.example.onlinelearning.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CourseController {
    @Autowired
    private CourseService courseService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private StatusService statusService;

    @Autowired
    private LessonService lessonService;


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
    public String saveCourse(Course course) {
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

    // SUBJECT LIST DEFAULT
    @GetMapping("/manage-courses")
    public String manageCourses(@AuthenticationPrincipal MyUserDetail userDetail, Model model, @RequestParam(value = "search", defaultValue = "") String searchInput, @RequestParam(value = "category", defaultValue = "-1") Integer categoryId, @RequestParam(value = "status", defaultValue = "-1") Integer statusId) {
        return viewManageCoursesPage(userDetail, model, searchInput, categoryId, statusId, 1);
    }

    // SUBJECT LIST PAGINATION
    @GetMapping("/manage-courses/{pageNumber}")
    public String viewManageCoursesPage(@AuthenticationPrincipal MyUserDetail userDetail, Model model, @RequestParam(value = "search", defaultValue = "") String searchInput, @RequestParam(value = "category", defaultValue = "-1") Integer categoryId, @RequestParam(value = "status", defaultValue = "-1") Integer statusId, @PathVariable(name = "pageNumber") int currentPage) {
        User currentUser = userDetail.getUser();
        Page<Course> page;

        // ROLE ADMIN
        // => Show all courses
        if (userDetail != null && userDetail.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
            page = courseService.filterCoursesByAdmin(searchInput, categoryId, statusId, currentPage);

        }
        // ROLE TEACHER
        // -> Show owned courses
        else {
            page = courseService.filterCoursesByTeacher(currentUser, searchInput, categoryId, statusId, currentPage);
        }
        long totalItems = page.getTotalElements();
        int totalPages = page.getTotalPages();
        List<Course> courseList = page.getContent();
        model.addAttribute("courseList", courseList);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("query", "/?search=" + searchInput + "&category=" + categoryId + "&status=" + statusId);
        model.addAttribute("categoryList", categoryService.findAll());
        model.addAttribute("statusList", statusService.findAll());
        model.addAttribute("lessonService", lessonService);
        model.addAttribute("currentCategoryId", categoryId);
        model.addAttribute("currentStatusId", statusId);
        model.addAttribute("currentSearch", searchInput);
        return "manage_course_list";


    }

}
