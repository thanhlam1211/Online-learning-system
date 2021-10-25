package com.example.onlinelearning.controller;

import com.example.onlinelearning.config.Utility;
import com.example.onlinelearning.entity.*;
import com.example.onlinelearning.repository.BarChartRepository;
import com.example.onlinelearning.repository.DashBoardRepository;
import com.example.onlinelearning.repository.RoleRepository;
import com.example.onlinelearning.repository.StatusRepository;
import com.example.onlinelearning.security.MyUserDetail;
import com.example.onlinelearning.service.CategoryService;
import com.example.onlinelearning.service.CourseService;
import com.example.onlinelearning.service.DashBoardService;
import com.example.onlinelearning.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.LinkedHashMap;
import java.util.List;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Admin
 */
@Controller
public class UserController {
    @Autowired
    private UserService service;

    @Autowired
    private CourseService courseService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DashBoardService dashBoardService;

    @Autowired
    private DashBoardRepository dashBoardRepository;

    @Autowired
    private BarChartRepository barChartRepository;

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute(name = "user") User user, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        Role roleUser = roleRepository.findByName("ROLE_USER");
        Status statusUser = statusRepository.findByValue("INACTIVE");
        user.setStatus(statusUser);
        user.addRole(roleUser);

        service.saveUser(user);
        String siteUrl = Utility.getSiteURL(request);
        service.sendVerificationEmail(user, siteUrl);

        return "verify";
    }

    // Account của từng user
    @GetMapping("/user_home")
    public String viewUserHome(@AuthenticationPrincipal MyUserDetail userDetail, Model model) {
        User user = userDetail.getUser();
        model.addAttribute("user", user);
        return "user_home";
    }

    @GetMapping("/user_home/update")
    public String viewUserEdit(@AuthenticationPrincipal MyUserDetail userDetail, Model model) {
        User user = userDetail.getUser();
        model.addAttribute("user", user);
        return "user_update";
    }

    @RequestMapping("/piechartdata")
    public ResponseEntity<?> getDataForPiechart(){
        List<CountCourse> piechartData = dashBoardRepository.countCourseByCategory();
        return new ResponseEntity<>(piechartData, HttpStatus.OK);
    }

    //Admin Home
    @GetMapping("/admin_home")
    public String viewHomePage(@AuthenticationPrincipal MyUserDetail myUserDetail, Model model) {
        User user = myUserDetail.getUser();
        // Get list of number of users
        List<User> allUsers = service.getAllUsers();
        List<User> studentAll = service.getUserByRole(3,1);
        List<User> teacherAll = service.getUserByRole(2,1);
        List<Course> allCourse = courseService.findAll();

        model.addAttribute("user", user);
        // Get list of course and count
        List<CountCourse> pieChart = dashBoardRepository.countCourseByCategory();
        List<BarChart> barCharts = barChartRepository.countCourseByTag();

        Map<String, Integer> surveyMap = new LinkedHashMap<>();
        int max = 0;
        // Draw barchart
        for(int i = 0; i<barCharts.size();i++){
            if(barCharts.get(i).getCountCourse() > max){
                max = barCharts.get(i).getCountCourse();
            }
            surveyMap.put(barCharts.get(i).getTagName(), barCharts.get(i).getCountCourse());
        }

        model.addAttribute("max", max);
        model.addAttribute("surveyMap", surveyMap);
        model.addAttribute("pieChart",pieChart);
        model.addAttribute("size", allUsers.size());
        model.addAttribute("sizeStudent", studentAll.size());
        model.addAttribute("sizeTeacher", teacherAll.size());
        model.addAttribute("sizeCourse", allCourse.size());

        return "Admin_Homepage";
    }

    //Admin Site
    @GetMapping("/admin_user_list")
    public String viewUserList(Model model, String keyword) {
        if(keyword != null) {
            model.addAttribute("userList", service.findByKeyword(keyword));
        }
        else {
            model.addAttribute("userList", service.getAllUsers());
        }
        return "Admin_user_list";
    }

    //Save change for user (admin)
    @PostMapping("/addUser")
    public String addUser(User user, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        service.saveUserWithDefaultRole(user);

        String siteUrl = Utility.getSiteURL(request);
        service.sendVerificationEmail(user, siteUrl);
        return "redirect:/admin_home";
    }

    //Get user in4 for edit page (admin)
    @GetMapping("/edit/{id}")
    public String viewUserEdit(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
        User user = service.getUserById(id);
        List<Role> listRoles = service.getRoles();
        List<Status> listStatus = service.getStatus();

        model.addAttribute("userDetail", user);
        model.addAttribute("listRoles", listRoles);
        model.addAttribute("listStatus", listStatus);
        return "Admin_user_edit";
    }

    //User detail (admin)
    @GetMapping("/details/{id}")
    public String showDetails(@PathVariable("id") Integer id, Model model) {
        User user = service.getUserById(id);
        model.addAttribute("userdt", user);
        return "Admin_user_details";
    }

    //User update (admin)
    @PostMapping("/update/{id}")
    public String saveUpdate(@PathVariable("id") int id, User user) {
        service.updateUser(id, user);
        return "redirect:/admin_home";
    }

    @PostMapping("/user_home/update/{id}")
    public String userUpdate(@RequestParam("file") MultipartFile file,
                             @AuthenticationPrincipal MyUserDetail userDetail,
                             @ModelAttribute("user") User user,
                             Model model) {
        //save user
        User existUser = userDetail.getUser();
        Path path = Paths.get("uploads/");
        if(file.isEmpty()) {
            return "user_home";
        }
        try{
            InputStream inputStream = file.getInputStream();
            Files.copy(inputStream, path.resolve(Objects.requireNonNull(file.getOriginalFilename())), StandardCopyOption.REPLACE_EXISTING);
            existUser.setAvatar(file.getOriginalFilename().toLowerCase());
        }catch (Exception e) {
            e.printStackTrace();
        }
        existUser.setFullName(user.getFullName());
        existUser.setGender(user.getGender());
        existUser.setPhone(user.getPhone());

        service.updateUser(existUser);
        model.addAttribute("user", existUser);
        return "user_home";
    }

    @GetMapping("/user_home/changePass")
    public String changePass(@AuthenticationPrincipal MyUserDetail userDetail, Model model) {
        User user = userDetail.getUser();
//        model.addAttribute("msg", "");
        return "change_password_form";
    }

    @PostMapping("/user_home/change_password")
    public String changePassWord(@AuthenticationPrincipal MyUserDetail userDetail, Model model
            , @RequestParam("oldPassWord") String oldPassword, @RequestParam("newPassWord") String newPassword
            , @RequestParam("retypePass") String retypePass
    ) {
        User user = userDetail.getUser();
        if (this.bCryptPasswordEncoder.matches(oldPassword, user.getPassword())
            && newPassword.equals(retypePass)
        ) {
            // Change password, return user_home
                service.updatePassword(user, newPassword);
                model.addAttribute("user", user);
                model.addAttribute("msg","Change password successfully");
                return "user_home";
        } else {
            model.addAttribute("msg","Incorrect password");
            return "change_password_form";
        }
    }
}
