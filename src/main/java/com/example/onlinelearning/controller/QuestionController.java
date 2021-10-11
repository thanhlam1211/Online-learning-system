package com.example.onlinelearning.controller;

import com.example.onlinelearning.entity.Category;
import com.example.onlinelearning.entity.QuestionBank;
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

    @RequestMapping("/questionList")
    public String questionPage (Model model) {
        String keyword = null;
        return listQuestionPage(model, 1, keyword);
    }

    @GetMapping("/questionList/{pageNumber}")
    public String listQuestionPage(Model model,
                                   @PathVariable(name="pageNumber") int currentPage,
                                   @Param("keyword") String keyword) {
        List<Category> categoryList = categoryService.getAll();
        Page<QuestionBank> page = service.listAll(currentPage, keyword);
        long totalItems = page.getTotalElements();
        int totalPages = page.getTotalPages();

        List<QuestionBank> questionList = page.getContent();

        model.addAttribute("currentPage",currentPage);
        model.addAttribute("totalItems",totalItems);
        model.addAttribute("totalPages",totalPages);
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("questionList", questionList);
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
