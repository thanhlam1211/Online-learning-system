package com.example.onlinelearning.controller;

import com.example.onlinelearning.entity.*;
import com.example.onlinelearning.repository.CourseRepository;
import com.example.onlinelearning.repository.QuizLevelRepository;
import com.example.onlinelearning.repository.QuizTypeRepository;
import com.example.onlinelearning.security.MyUserDetail;
import com.example.onlinelearning.service.CategoryService;
import com.example.onlinelearning.service.QuizService;
import com.example.onlinelearning.service.UserQuestionAnswerService;
import com.example.onlinelearning.service.UserQuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
@Controller
public class QuizController {
    @Autowired
    private QuizService quizService;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private QuizLevelRepository quizLevelRepository;
    @Autowired
    private QuizTypeRepository quizTypeRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CourseController courseController;
    @Autowired
    private UserQuizService userQuizService;
    @Autowired
    private UserQuestionAnswerService userQuestionAnswerService;
    //Quiz
    @GetMapping("/quiz")
    public String viewQuiz(Model model,
                           @RequestParam(value = "search", defaultValue = "") String searchInput,
                           @RequestParam(value = "type", defaultValue = "-1") Integer typeId,
                           @RequestParam(value = "level", defaultValue = "-1") Integer levelId) {
        return listQuizByPages(model, searchInput, typeId, levelId, 1);
    }

    @GetMapping("/quiz/{pageNumber}")
    public String listQuizByPages(Model model,
                                  @RequestParam(value = "search ", defaultValue = "") String searchInput,
                                  @RequestParam(value = "type", defaultValue = "-1") Integer typeId,
                                  @RequestParam(value = "level", defaultValue = "-1") Integer levelId,
                                  @PathVariable(name = "pageNumber") int currentPage) {
        Page<Quiz> page = quizService.listAll(currentPage, searchInput, typeId, levelId);
        long totalItems = page.getTotalElements();
        int totalPages = page.getTotalPages();
        List<Quiz> listQuiz = page.getContent();
        model.addAttribute("query", "/?search=" + searchInput + "&level=" + levelId + "&type=" + typeId);
        model.addAttribute("listQuizLevel", quizLevelRepository.findAll());
        model.addAttribute("listQuizType", quizTypeRepository.findAll());
        model.addAttribute("listCategory", categoryService.findAll());
        model.addAttribute("currentSearch", searchInput);
        model.addAttribute("currentLevel", levelId);
        model.addAttribute("currentType", typeId);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("listQuiz", listQuiz);
        return "quiz";
    }

    @GetMapping("/quiz_detail/{id}")
    public ModelAndView viewQuizDetail(@PathVariable(name = "id") Integer id) {
        ModelAndView modelAndView = new ModelAndView("quiz_detail");
        Quiz quiz = quizService.getQuizById(id);
        modelAndView.addObject("listQuizLevel", quizLevelRepository.findAll());
        modelAndView.addObject("listQuizType", quizTypeRepository.findAll());
        modelAndView.addObject("quiz", quiz);
        return modelAndView;
    }

    @PostMapping("/updateQuiz")
    public String updateQuiz(@ModelAttribute("quiz") Quiz quiz, Model model){
        quizService.saveQuiz(quiz);
        return viewQuiz(model,"",-1,-1);
    }

//    @PostMapping("/deleteQuiz/{id}")
//    public String deleteQuiz(@PathVariable(name = "id") Integer id, Model model){
//        quizService.deleteQuiz(id);
//        return viewQuiz(model);
//    }

    @GetMapping("/add_quiz/{id}")
    public ModelAndView addQuiz(@PathVariable(name = "id") Integer id) {
        ModelAndView modelAndView = new ModelAndView("add_quiz");
        Quiz quiz = new Quiz();
        quiz.setCourse(courseRepository.getById(id));
        modelAndView.addObject("listQuizLevel", quizLevelRepository.findAll());
        modelAndView.addObject("listQuizType", quizTypeRepository.findAll());
        modelAndView.addObject("quiz", quiz);
        modelAndView.addObject("course", courseRepository.getById(id));
        return modelAndView;
    }

    @PostMapping("/saveQuiz")
    public String saveQuiz(@ModelAttribute("quiz") Quiz quiz,
                                 @ModelAttribute(name = "courseid") Integer id){
        quiz.setCourse(courseRepository.getById(id));
        quizService.saveQuiz(quiz);
        return "redirect:/course_detail/"+id;
    }

    @GetMapping("/attend_quiz/{quiz_id}")
    public ModelAndView attendQuiz(@PathVariable(name = "quiz_id") Integer quiz_id, @AuthenticationPrincipal MyUserDetail userDetail) {
        ModelAndView modelAndView = new ModelAndView("attend_quiz");
        User user = userDetail.getUser();
        Quiz quiz = quizService.getQuizById(quiz_id);
        List<UserQuiz> userQuiz = userQuizService.getUserQuizByQuiz_IdAndUser_Id(quiz.getId(),user.getId());
        float highestMark = 0;
        for (UserQuiz uq: userQuiz) {
            if(uq.getMark()>=highestMark){
                highestMark=uq.getMark();
            }
        }
        modelAndView.addObject("userQuiz", userQuiz);
        modelAndView.addObject("highestMark", highestMark);
        modelAndView.addObject("quiz", quiz);
        return modelAndView;
    }

    @GetMapping("/quiz_review/{quiz_id}")
    public ModelAndView reviewQuiz(@PathVariable(name = "quiz_id") Integer quiz_id, @AuthenticationPrincipal MyUserDetail userDetail) {
        ModelAndView modelAndView = new ModelAndView("quiz_review");
        User user = userDetail.getUser();
        Quiz quiz = quizService.getQuizById(quiz_id);
        List<UserQuiz> userQuiz = userQuizService.getUserQuizByQuiz_IdAndUser_Id(quiz.getId(),user.getId());
        float highestMark = 0;
        int lastId = 0;
        UserQuiz lastUserQuiz = new UserQuiz();
        for (UserQuiz uq: userQuiz) {
            if(uq.getMark()>=highestMark){
                highestMark=uq.getMark();
            }
            if(uq.getId()>=lastId){
                lastUserQuiz=uq;
            }
        }
        List<UserQuestionAnswer> userQuestionAnswer = userQuestionAnswerService.getUserQuestionAnswersByUserQuizOrderById(lastUserQuiz.getId());
        modelAndView.addObject("lastUserQuiz", lastUserQuiz);
//        modelAndView.addObject("userQuestionAnswer", userQuestionAnswer);
        modelAndView.addObject("highestMark", highestMark);
        modelAndView.addObject("quiz", quiz);
        return modelAndView;
    }
}
