package com.example.onlinelearning.controller;

import com.example.onlinelearning.entity.*;
import com.example.onlinelearning.repository.LessonRepository;
import com.example.onlinelearning.repository.StatusRepository;
import com.example.onlinelearning.repository.TopicRepository;
import com.example.onlinelearning.security.MyUserDetail;
import com.example.onlinelearning.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedHashMap;
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
    private CourseService courseService;

    @Autowired
    private TopicService topicService;

    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private LessonRepository lessonRepository;

    // SUBJECT Lesson DEFAULT
    // Trung Đức làm phần này
    @GetMapping("/manage-lessons/course{currentCourseId}")
    public String manageLessons(@AuthenticationPrincipal MyUserDetail userDetail, Model model,
                                @RequestParam(value = "search", defaultValue = "") String searchInput,
                                @RequestParam(value = "topic", defaultValue = "-1") Integer topicId,
                                @RequestParam(value = "status", defaultValue = "-1") Integer statusId,
                                @PathVariable("currentCourseId") Integer currentCourseId) {
        return viewManageLessonsPage(userDetail, model, searchInput, topicId, statusId, currentCourseId,1);
    }

    // Subject Lesson Pagination
    // Trung Đức làm phần này
    // Phân trang của subject lesson theo các filter đã có
    @GetMapping("/manage-lessons/course{currentCourseId}/{pageNumber}")
    private String viewManageLessonsPage(@AuthenticationPrincipal MyUserDetail userDetail, Model model,
                                         @RequestParam(value = "search", defaultValue = "") String searchInput,
                                         @RequestParam(value = "topic", defaultValue = "-1") Integer topicId,
                                         @RequestParam(value = "status", defaultValue = "-1") Integer statusId,
                                         @PathVariable("currentCourseId") Integer currentCourseId,
                                         @PathVariable(name = "pageNumber") int currentPage) {
        User currentUser = userDetail.getUser();
        Page<Lesson> page;
        page = lessonService.filterLesson(currentCourseId, searchInput, topicId, statusId, currentPage);
        long totalItems = page.getTotalElements();
        int totalPages = page.getTotalPages();
        List<Lesson> lessonList = page.getContent();

        // Add attribute use in page
        model.addAttribute("lessonList", lessonList);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", currentPage);

        model.addAttribute("query", "/?search=" + searchInput + "&topic=" + topicId + "&status=" + statusId);
//        http://localhost:8081/manage-lessons/course?search=tieng&currentCourseId=1&topic=1&status=1
        model.addAttribute("topicList", topicService.findAllByCourse_Id(currentCourseId));
        model.addAttribute("statusList", statusService.findAll());
        model.addAttribute("currentCourseId", currentCourseId);
        model.addAttribute("currentTopicId", topicId);
        model.addAttribute("currentStatusId", statusId);
        model.addAttribute("currentSearch", searchInput);

        return "manage_lesson_list";
    }

    // ADD NEW LESSON MODAL
    @GetMapping("/manage-lessons/course{courseId}/add")
    public String addNewLesson(Model model, @PathVariable(name = "courseId") Integer courseId) {
        List<Topic> topicList = topicService.findAllByCourse_Id(courseId);
        model.addAttribute("topicList", topicList);
        model.addAttribute("courseId", courseId);
        return "add_lesson_modal";
    }

    // ADD NEW TOPIC
    @PostMapping("/manage-lessons/course{courseId}/add/topic")
    public String addTopic(Model model, @PathVariable(name = "courseId") Integer courseId, Topic topic) {
        Course currentCourse = courseService.getCourseById(courseId);
        topic.setCourse(currentCourse);
        topicRepository.save(topic);
        return "redirect:/manage-lessons/course" + courseId;
    }

    // ADD NEW LESSON
    @PostMapping("/manage-lessons/course{courseId}/add/lesson")
    public String addLesson(Model model, @PathVariable(name = "courseId") Integer courseId, Lesson lesson) {
        Course currentCourse = courseService.getCourseById(courseId);
        Status status = statusRepository.findByValue("ACTIVE");
        lesson.setCourse(currentCourse);
        lesson.setStatus(status);
        lessonRepository.save(lesson);
        return "redirect:/manage-lessons/course" + courseId;
    }

    // EDIT LESSON MODAL
    @GetMapping("/manage-lessons/course{courseId}/edit/lesson/{lessonId}")
    public String editLesson(Model model, @PathVariable(name = "lessonId") Integer lessonId, @PathVariable(name = "courseId") Integer courseId) {
        Lesson currentLesson = lessonRepository.getById(lessonId);
        List<Topic> topicList = topicService.findAllByCourse_Id(courseId);
        model.addAttribute("topicList", topicList);
        model.addAttribute("currentLesson", currentLesson);
        return "edit_lesson_modal";
    }

    @PostMapping("/manage-lessons/course{courseId}/edit/lesson/{lessonId}")
    public String saveLessonChanges(Model model, @PathVariable(name = "lessonId") Integer lessonId, @PathVariable(name = "courseId") Integer courseId, Lesson lesson) {
        Lesson originalLesson = lessonRepository.getById(lessonId);
        lesson.setStatus(originalLesson.getStatus());
        lesson.setCourse(originalLesson.getCourse());
        lessonRepository.save(lesson);
        return "redirect:/manage-lessons/course" + courseId;
    }

    // LESSON VIEW
    // Mode: Learning Default
    @GetMapping("/learning/course/{courseId}")
    public String lessonViewDefault(@AuthenticationPrincipal MyUserDetail userDetail, Model model, @PathVariable(name = "courseId") Integer courseId) {
        // Get first lessonId
        Integer firstLessonId = lessonService.getFirstLessonId(courseId);
        return lessonView(userDetail, model, courseId, firstLessonId);
    }

    // LESSON VIEW
    // Mode: Learning
    @GetMapping("/learning/course/{courseId}/lesson/{lessonId}")
    public String lessonView(@AuthenticationPrincipal MyUserDetail userDetail, Model model, @PathVariable(name = "courseId") Integer courseId, @PathVariable(name = "lessonId") Integer lessonId) {
        // Get current User
        User currentUser = userDetail.getUser();

        // Get courseContent sorted by 'Order' field
        LinkedHashMap<Topic, List<Lesson>> courseContent = lessonService.getCourseContentByCourseId(courseId);

        // Get currentCourse
        Course currentCourse = courseService.getCourseById(courseId);

        // Check if user has registered this course or not?
//        boolean flag = false;
//        for (UserCourse userCourse : currentUser.getUserCourseList()) {
//            if (userCourse.getCourse().getId() == courseId) {
//                flag = true;
//                break;
//            }
//        }
//        if (!flag) return "redirect:/";

        // Get currentLesson
        Lesson currentLesson = new Lesson();
        if (lessonId != -1) {
           currentLesson = lessonRepository.getById(lessonId);
        }

        // Get youtube embed link
        String embedLink = "";
        try {
            String[] parts = currentLesson.getVideoLinkId().split("\\?v=");
             embedLink = "https://www.youtube.com/embed/" + parts[1];
        } catch (Exception e) {}

        // Get current topicId
        Integer currentTopicId = -1;
        if (lessonId != -1) {
            currentTopicId = currentLesson.getTopic().getTopicId();
        }

        // Next lessonId
        Integer nextLessonId = lessonService.getNextLessonId(lessonId, courseContent);
        // Previous lessonId
        Integer previousLessonId = lessonService.getPreviousLessonId(lessonId, courseContent);

        // Assign to Model
        model.addAttribute("lessonId", lessonId);
        model.addAttribute("nextLessonId", nextLessonId);
        model.addAttribute("previousLessonId", previousLessonId);
        model.addAttribute("currentCourse", currentCourse);
        model.addAttribute("embedLink", embedLink);
        model.addAttribute("courseContent", courseContent);
        model.addAttribute("currentLesson", currentLesson);
        model.addAttribute("currentTopicId", currentTopicId);
        return "lesson";
    }


}
