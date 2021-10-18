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
import org.springframework.web.bind.annotation.*;
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
                                @RequestParam(value = "status", defaultValue = "-1") Integer statusId) {
        return listQuestionPage(model, 1, courseId, statusId, "");
    }

    @GetMapping("/questionList/{pageNumber}")
    public String listQuestionPage(Model model,
                                   @PathVariable(name="pageNumber") int currentPage,
                                   @RequestParam(value = "course", defaultValue = "-1") Integer courseId,

                                   @RequestParam(value = "status", defaultValue = "-1") Integer statusId,
                                   @RequestParam(value = "keyword", defaultValue = "") String keyword ) {
        List<Category> categoryList = categoryService.getAll();
        Page<QuestionBank> page = service.listAll(currentPage, keyword, courseId, statusId);
        long totalItems = page.getTotalElements();
        int totalPages = page.getTotalPages();

        List<QuestionBank> questionList = page.getContent();

        model.addAttribute("currentPage",currentPage);
        model.addAttribute("totalItems",totalItems);
        model.addAttribute("totalPages",totalPages);
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("questionList", questionList);
        model.addAttribute("levelList", levelRepository.findAll());
        model.addAttribute("statusList", statusRepository.findAll());
        model.addAttribute("dimensionList", dimensionRepository.findAll());
        model.addAttribute("courseList", courseRepository.findAll());
        model.addAttribute("query", "/?keyword=" + keyword + "&course=" + courseId + "&status=" + statusId);

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

    @GetMapping("/questionEditModal/{id}")
    public ModelAndView viewEditQuestion(@PathVariable(name = "id") Integer id) {
        ModelAndView modelAndView = new ModelAndView("question-edit-modal.component");
        QuestionBank question = service.getQuestionById(id);
        modelAndView.addObject("listCategory", categoryService.findAll());
        modelAndView.addObject("question", question);
        modelAndView.addObject("statusList", statusRepository.findAll());
        modelAndView.addObject("levelList", levelRepository.findAll());
        modelAndView.addObject("dimensionList", dimensionRepository.findAll());
        return modelAndView;
    }
    @PostMapping("/updateQuestion")
    public String updateQuestion(@ModelAttribute("question") QuestionBank questionBank, Model model) {
        Integer questionId = questionBank.getId();
        QuestionBank newQuestion = service.getQuestionById(questionId);
        newQuestion.setId(questionBank.getId());
        newQuestion.setContent(questionBank.getContent());
        newQuestion.setOption1(questionBank.getOption1());
        newQuestion.setOption2(questionBank.getOption2());
        newQuestion.setOption3(questionBank.getOption3());
        newQuestion.setOption4(questionBank.getOption4());
        newQuestion.setAnswer(questionBank.getAnswer());
        newQuestion.setExplanation(questionBank.getExplanation());
        newQuestion.setStatus(questionBank.getStatus());
        newQuestion.setQuizLevel(questionBank.getQuizLevel());
        service.saveQuestion(newQuestion);
        return listQuestionPage(model, 1, -1, -1, "");
    }

    @GetMapping("/addQuestion")
    public ModelAndView addQuestion() {
        ModelAndView modelAndView = new ModelAndView("add-question-modal.component");
        QuestionBank question = new QuestionBank();
        modelAndView.addObject("listCategory", categoryService.findAll());
        modelAndView.addObject("statusList", statusRepository.findAll());
        modelAndView.addObject("levelList", levelRepository.findAll());
        modelAndView.addObject("courseList", courseRepository.findAll());
        modelAndView.addObject("dimensionList", dimensionRepository.findAll());
        modelAndView.addObject("question", question);
        return modelAndView;
    }

    @PostMapping("/addQuestion")
    public String processAddQuestion(@ModelAttribute("question") QuestionBank questionBank, Model model) {
        service.saveQuestion(questionBank);
        return listQuestionPage(model, 1, -1, -1, "");
    }
}
