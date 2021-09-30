package com.example.onlinelearning.controller;

import com.example.onlinelearning.repository.RoleRepository;
import com.example.onlinelearning.repository.UserRepository;
import com.example.onlinelearning.security.MyUserDetail;
import com.example.onlinelearning.service.UserService;
import com.example.onlinelearning.entity.Role;
import com.example.onlinelearning.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Admin
 */
@Controller
public class UserController {
    @Autowired
    private UserService service;
    @Autowired
    private RoleRepository repository;
    @Autowired
    private UserRepository userRepo;

    @GetMapping("/process_register")
    public String createNewUser(Model model){
        User user = new User();
        model.addAttribute("user",user);
        return "sign_up";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute(name = "user") User user){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        Role roleUser = repository.findByName("ROLE_USER");
        user.addRole(roleUser);
        service.saveUser(user);
        return "login";
    }

    @GetMapping("/user_home")
    public String viewUserHome(@AuthenticationPrincipal MyUserDetail userDetail, Model model){
        model.addAttribute("user",userDetail);
        return "/user_home";
    }

    //Admin Site
    @GetMapping("/admin_home")
    public String viewAdminPage(Model model) {
        model.addAttribute("userList", service.getAllUsers());
        return "Admin Homepage";
    }

    //Delete user
    @GetMapping("/delete")
    public String deleteUser(Integer id) {
        userRepo.deleteById(id);
        return "redirect:/";
    }

    //Save change for user (admin)
    @PostMapping("/saveChange")
    public String saveUserChange(@ModelAttribute(name = "user") User user){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        Role roleUser = repository.findByName("ROLE_USER");
        user.addRole(roleUser);
        service.saveUser(user);
        return "Admin Homepage";
    }

}
