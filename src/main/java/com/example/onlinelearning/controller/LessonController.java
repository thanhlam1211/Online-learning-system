package com.example.onlinelearning.controller;

import com.example.onlinelearning.entity.Course;
import com.example.onlinelearning.entity.Lesson;
import com.example.onlinelearning.entity.User;
import com.example.onlinelearning.repository.StatusRepository;
import com.example.onlinelearning.security.MyUserDetail;
import com.example.onlinelearning.service.CategoryService;
import com.example.onlinelearning.service.LessonService;
import com.example.onlinelearning.service.StatusService;
import com.example.onlinelearning.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class LessonController {

    @Autowired
    private LessonService lessonService;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private StatusService statusService;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TopicService topicService;

    // SUBJECT Lesson DEFAULT
    @GetMapping("/manage-lessons/course{id}")
    public String manageLessons(@AuthenticationPrincipal MyUserDetail userDetail, Model model,
                                @RequestParam(value = "search", defaultValue = "") String searchInput,
                                @RequestParam(value = "topic", defaultValue = "-1") Integer topicId,
                                @RequestParam(value = "status", defaultValue = "-1") Integer statusId,
                                @PathVariable("id") Integer id) {
        return viewManageLessonsPage(userDetail, model, searchInput, topicId, statusId, id,1);
    }

    // Subject Lesson Pagination
    @GetMapping("/manage-lessons/course{id}/{pageNumber}")
    private String viewManageLessonsPage(@AuthenticationPrincipal MyUserDetail userDetail, Model model,
                                         @RequestParam(value = "search", defaultValue = "") String searchInput,
                                         @RequestParam(value = "topic", defaultValue = "-1") Integer topicId,
                                         @RequestParam(value = "status", defaultValue = "-1") Integer statusId,
                                         @PathVariable("id") Integer id,
                                         @PathVariable(name = "pageNumber") int currentPage) {
        User currentUser = userDetail.getUser();
        Page<Lesson> page;
        page = lessonService.filterLesson(id, searchInput, topicId, statusId, currentPage);
        long totalItems = page.getTotalElements();
        int totalPages = page.getTotalPages();
        List<Lesson> lessonList = page.getContent();

        // Add attribute use in page
        model.addAttribute("lessonList", lessonList);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", currentPage);

        model.addAttribute("query", "/?search=" + searchInput + "&topic=" + topicId + "&status=" + statusId);
        model.addAttribute("topicList", topicService.findAllByCourse_Id(id));
        model.addAttribute("statusList", statusService.findAll());
        model.addAttribute("currentCourseId", id);
        model.addAttribute("currentTopicId", topicId);
        model.addAttribute("currentStatusId", statusId);
        model.addAttribute("currentSearch", searchInput);

        return "manage_lesson_list";
    }
}
