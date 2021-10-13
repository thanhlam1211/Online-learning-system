package com.example.onlinelearning.controller;

import com.example.onlinelearning.entity.Course;
import com.example.onlinelearning.entity.Quiz;
import com.example.onlinelearning.repository.CourseRepository;
import com.example.onlinelearning.repository.QuizLevelRepository;
import com.example.onlinelearning.repository.QuizTypeRepository;
import com.example.onlinelearning.service.CategoryService;
import com.example.onlinelearning.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
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
    //Quiz
    @GetMapping("/quiz")
    public String viewQuiz(Model model) {
        String keyword = null;
        return listQuizByPages(model, 1, keyword);
    }

    @GetMapping("/quiz/page/{pageNumber}")
    public String listQuizByPages(Model model,
                                  @PathVariable(name = "pageNumber") int currentPage,
                                  @Param("keyword") String keyword) {
        Page<Quiz> page = quizService.listAll(currentPage, keyword);
        long totalItems = page.getTotalElements();
        int totalPages = page.getTotalPages();
        List<Quiz> listQuiz = page.getContent();
        model.addAttribute("listCategory", categoryService.findAll());
        model.addAttribute("keyword", keyword);
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
        return "redirect:/quiz";
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
    public ModelAndView saveQuiz(@ModelAttribute("quiz") Quiz quiz, @ModelAttribute(name = "courseid") Integer id){
        quiz.setCourse(courseRepository.getById(id));
        quizService.saveQuiz(quiz);
        return courseController.viewCourseDetail(id);
    }
}
