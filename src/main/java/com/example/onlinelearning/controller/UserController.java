package com.example.onlinelearning.controller;

import com.example.onlinelearning.repository.RoleRepository;
import com.example.onlinelearning.security.MyUserDetail;
import com.example.onlinelearning.service.UserService;
import com.example.onlinelearning.entity.Role;
import com.example.onlinelearning.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Admin
 */
@Controller
public class UserController {
    @Autowired
    private UserService service;
    @Autowired
    private RoleRepository repository;

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


    // Account của từng user
    @GetMapping("/account/{username}")
    public ModelAndView accountUser(@PathVariable(name = "username") String username){
        ModelAndView modelAndView = new ModelAndView("account");
        User user = service.getUserByName(username);
        modelAndView.addObject("user", user);
        return modelAndView;
    }
    //Điều hướng sang trang login
    @RequestMapping("/login")
    public String login(Model model){
        User user = new User();
        model.addAttribute("alert");
        model.addAttribute("user",user);
        return "login";
    }
    //Check login
    @RequestMapping("/verify")
    public String verify(@ModelAttribute("user") User user, Model model){
        if(service.getUserByName(user.getUsername()) == null) {
            model.addAttribute("alert","Your account isn't existed");
            model.addAttribute("user",user);
            return "login";
        }
        else if(!service.getUserByName(user.getUsername()).getPassword().equals(user.getPassword())){
            model.addAttribute("alert","Wrong Password");
            model.addAttribute("user",user);
            return "login";
        }
        else{
            return "redirect:/";
        }
    }
}
