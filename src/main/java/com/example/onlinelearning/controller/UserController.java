package com.example.onlinelearning.controller;

import com.example.onlinelearning.config.Utility;
import com.example.onlinelearning.entity.Category;
import com.example.onlinelearning.entity.Status;
import com.example.onlinelearning.repository.CourseRepository;
import com.example.onlinelearning.repository.RoleRepository;
import com.example.onlinelearning.repository.StatusRepository;
import com.example.onlinelearning.security.MyUserDetail;
import com.example.onlinelearning.service.CategoryService;
import com.example.onlinelearning.service.UserCourseService;
import com.example.onlinelearning.service.UserService;
import com.example.onlinelearning.entity.Role;
import com.example.onlinelearning.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
/**
 * @author Admin
 */
@Controller
public class UserController {
    @Autowired
    private UserService service;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserCourseService userCourseService;

    @Autowired
    private CourseRepository courseRepository;

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

    //Admin Site
    @GetMapping("/admin_home")
    public String viewAdminPage(Model model, String keyword) {
        if(keyword != null) {
            model.addAttribute("userList", service.findByKeyword(keyword));
        }
        else {
            model.addAttribute("userList", service.getAllUsers());
        }
        return "Admin_Homepage";
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

    @GetMapping("/myRegistration")
    public String viewRegistration(@AuthenticationPrincipal MyUserDetail userDetail, Model model) {
        User user = userDetail.getUser();
        List<Category> categoryList = categoryService.getAll();
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("courseRegister", userCourseService.getListCourseByUserId(user.getId()));
        return "my-registration";
    }

    @GetMapping("/myCourse")
    public String viewCourse(@AuthenticationPrincipal MyUserDetail userDetail, Model model) {
        User user = userDetail.getUser();
        List<Category> categoryList = categoryService.getAll();
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("myCourse", userCourseService.getListCourseByUserId(user.getId()));
        return "my-course";
    }

    @GetMapping("/registrationList")
    public String viewRegistrationList(Model model) {
        List<Category> categoryList = categoryService.getAll();
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("courseList", courseRepository.findAll());
        model.addAttribute("courseRegister", userCourseService.getListCourse());
        return "registration-list";
    }


}
