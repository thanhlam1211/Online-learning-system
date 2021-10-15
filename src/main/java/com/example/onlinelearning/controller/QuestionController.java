package com.example.onlinelearning.controller;

import com.example.onlinelearning.entity.Category;
import com.example.onlinelearning.entity.QuestionBank;
import com.example.onlinelearning.repository.CourseRepository;
import com.example.onlinelearning.repository.DimensionRepository;
import com.example.onlinelearning.repository.LevelRepository;
import com.example.onlinelearning.repository.StatusRepository;
import com.example.onlinelearning.service.CategoryService;
import com.example.onlinelearning.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @author Admin
 */
@Controller
public class QuestionController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private QuestionService service;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private LevelRepository levelRepository;
    @Autowired
    private DimensionRepository dimensionRepository;
    @Autowired
    private CourseRepository courseRepository;

    @RequestMapping("/questionList")
    public String questionPage (Model model,
                                @RequestParam(value = "course", defaultValue = "-1") Integer courseId,
                                @RequestParam(value = "dimension", defaultValue = "-1") Integer dimensionId,
                                @RequestParam(value = "level", defaultValue = "-1") Integer levelId,
                                @RequestParam(value = "status", defaultValue = "-1") Integer statusId) {
        return listQuestionPage(model, 1, courseId, dimensionId, levelId, statusId, null);
    }

    @GetMapping("/questionList/{pageNumber}")
    public String listQuestionPage(Model model,
                                   @PathVariable(name="pageNumber") int currentPage,
                                   @RequestParam(value = "course", defaultValue = "-1") Integer courseId,
                                   @RequestParam(value = "dimension", defaultValue = "-1") Integer dimensionId,
                                   @RequestParam(value = "level", defaultValue = "-1") Integer levelId,
                                   @RequestParam(value = "status", defaultValue = "-1") Integer statusId,
                                   @Param("keyword") String keyword) {
        List<Category> categoryList = categoryService.getAll();
        Page<QuestionBank> page = service.listAll(currentPage, keyword, courseId, dimensionId, levelId, statusId);
        long totalItems = page.getTotalElements();
        int totalPages = page.getTotalPages();

        List<QuestionBank> questionList = page.getContent();

        model.addAttribute("currentPage",currentPage);
        model.addAttribute("totalItems",totalItems);
        model.addAttribute("totalPages",totalPages);
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("questionList", questionList);
        model.addAttribute("statusList", statusRepository.findAll());
        model.addAttribute("levelList", levelRepository.findAll());
        model.addAttribute("dimensionList", dimensionRepository.findAll());
        model.addAttribute("courseList", courseRepository.findAll());

        return "questionList";
    }

    @GetMapping("/questionModal/{id}")
    public ModelAndView viewQuestion(@PathVariable(name = "id") Integer id) {
        ModelAndView modelAndView = new ModelAndView("question-detail-modal.component");
        QuestionBank question = service.getQuestionById(id);
        modelAndView.addObject("listCategory", categoryService.findAll());
        modelAndView.addObject("question", question);
        return modelAndView;
    }
}
