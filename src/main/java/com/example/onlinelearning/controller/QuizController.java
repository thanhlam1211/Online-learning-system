package com.example.onlinelearning.controller;

import com.example.onlinelearning.entity.*;
import com.example.onlinelearning.repository.*;
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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

//thanhlthe150044 made this
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
    private QuestionService questionService;
    @Autowired
    private CourseController courseController;
    @Autowired
    private UserQuizService userQuizService;
    @Autowired
    private UserQuestionAnswerService userQuestionAnswerService;
    @Autowired
    private UserQuizRepository userQuizRepository;
    @Autowired
    private UserQuestionAnswerRepository userQuestionAnswerRepository;

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
    public String updateQuiz(@ModelAttribute("quiz") Quiz quiz, Model model) {
        quizService.saveQuiz(quiz);
        return viewQuiz(model, "", -1, -1);
    }

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
                           @ModelAttribute(name = "courseid") Integer id) {
        quiz.setCourse(courseRepository.getById(id));
        quizService.saveQuiz(quiz);
        return "redirect:/course_detail/" + id;
    }

    @GetMapping("/attend_quiz/{quiz_id}")
    public ModelAndView attendQuiz(@PathVariable(name = "quiz_id") Integer quiz_id, @AuthenticationPrincipal MyUserDetail userDetail) {
        ModelAndView modelAndView = new ModelAndView("attend_quiz");
        User user = userDetail.getUser();
        Quiz quiz = quizService.getQuizById(quiz_id);
        List<UserQuiz> userQuiz = userQuizService.getUserQuizByQuiz_IdAndUser_Id(quiz.getId(), user.getId());
        float highestMark = 0;
        for (UserQuiz uq : userQuiz) {
            if (uq.getMark() >= highestMark) {
                highestMark = uq.getMark();
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
        List<UserQuiz> userQuiz = userQuizService.getUserQuizByQuiz_IdAndUser_Id(quiz.getId(), user.getId());
        float highestMark = 0;
        int lastId = 0;
        UserQuiz lastUserQuiz = new UserQuiz();
        for (UserQuiz uq : userQuiz) {
            if (uq.getMark() >= highestMark) {
                highestMark = uq.getMark();
            }
            if (uq.getId() >= lastId) {
                lastUserQuiz = uq;
            }
        }
        List<UserQuestionAnswer> userQuestionAnswer = userQuestionAnswerService.getUserQuestionAnswersByUserQuizOrderById(lastUserQuiz.getId());
        modelAndView.addObject("lastUserQuiz", lastUserQuiz);
        modelAndView.addObject("highestMark", highestMark);
        modelAndView.addObject("quiz", quiz);
        return modelAndView;
    }

    // QUIZ HANDLE
    @GetMapping("/quiz-handle/{quizId}")
    public String quizHandle(@AuthenticationPrincipal MyUserDetail userDetail, Model model, @PathVariable(name = "quizId") Integer quizId, HttpServletRequest request, HttpServletResponse response) {
        // Notice: If user tries to Reload the page
        // All answers won't be saved

        User currentUser = userDetail.getUser();
        UserQuiz currentUserQuiz;

        // Get current Quiz
        Quiz currentQuiz = quizService.getQuizById(quizId);

        // Check if User is already doing quiz
        Cookie[] cookies = request.getCookies();
        Integer currentUserQuizId = null;
        boolean flag = false;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("currentUserQuizId") && cookie.getValue() != null) {
                    currentUserQuizId = Integer.parseInt(cookie.getValue());
                    flag = true;
                }
            }
        }
        if (!flag) {
            // Insert to User_Quiz table
            currentUserQuiz = userQuizService.addUserQuiz(currentUser, currentQuiz);
            // Add to cookie
            Cookie currentUserQuizCookie = new Cookie("currentUserQuizId", currentUserQuiz.getId().toString());
            currentUserQuizCookie.setMaxAge(9999999);
            currentUserQuizCookie.setPath("/");
            response.addCookie(currentUserQuizCookie);
        } else {
            // Get existed UserQuiz
            currentUserQuiz = userQuizRepository.getById(currentUserQuizId);
        }

        // Get All Questions Of Quiz
        Set<QuestionBank> questionList = questionService.getAllQuestionsOfQuizId(quizId);

        // Get current Date Time
        Date currentTime = new Date();
        // Get time left (in seconds)
        Long diff = currentTime.getTime() - currentUserQuiz.getStartTime().getTime();
        Long timeLeft = 60 * currentQuiz.getDuration() - TimeUnit.MILLISECONDS.toSeconds(diff);

        model.addAttribute("timeLeft", timeLeft);
        model.addAttribute("currentQuiz", currentQuiz);
        model.addAttribute("currentUserQuiz", currentUserQuiz);
        model.addAttribute("questionList", questionList);
        return "quiz_handle";
    }

    @PostMapping("/quiz-handle/{quizId}/submit")
    public String quizHandleSubmit(Model model, @PathVariable(name = "quizId") Integer quizId, HttpServletRequest request, HttpServletResponse response) {
        // Get UserQuiz
        UserQuiz currentUserQuiz = userQuizRepository.getById(Integer.parseInt(request.getParameter("currentUserQuizId")));


        // Get All Questions Of Quiz
        Set<QuestionBank> questionList = questionService.getAllQuestionsOfQuizId(quizId);

        // Initialize mark
        float mark = 0;
        int numOfQuestions = questionList.size();
        float numOfRightAnswers = 0;

        for (QuestionBank question : questionList) {
            // Create UserQuestionAnswer instance
            UserQuestionAnswer newUserQuestionAnswer = new UserQuestionAnswer();
            newUserQuestionAnswer.setQuestionBank(question);
            newUserQuestionAnswer.setUserQuiz(currentUserQuiz);

            // User answered
            if (request.getParameter("q" + question.getId()) != null) {
                newUserQuestionAnswer.setUserChoice(request.getParameter("q" + question.getId()));
                if (request.getParameter("q" + question.getId()).equalsIgnoreCase(question.getAnswer()))
                    numOfRightAnswers += 1;
            }
            // User didn't answer
            else {
                newUserQuestionAnswer.setUserChoice("empty");
            }
            // Save
            userQuestionAnswerRepository.save(newUserQuestionAnswer);
        }

        // Calculate mark
        mark = numOfRightAnswers / numOfQuestions;

        // Update mark for currentUserQuiz
        currentUserQuiz.setMark(mark * 100);
        userQuizRepository.save(currentUserQuiz);

        return "redirect:/quiz-handle/delete-cookie/" + quizId;
    }

    @GetMapping("/quiz-handle/delete-cookie/{quizId}")
    public String quizHandleDeleteCookie(HttpServletResponse response, @PathVariable(name = "quizId") Integer quizId) {
        // Delete Cookie
        Cookie currentUserQuizCookie = new Cookie("currentUserQuizId", null);
        currentUserQuizCookie.setMaxAge(0);
        currentUserQuizCookie.setPath("/");
        response.addCookie(currentUserQuizCookie);

        return "redirect:/quiz_review/" + quizId;
    }
}
