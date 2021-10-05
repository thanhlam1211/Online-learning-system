package com.example.onlinelearning.controller;

import com.example.onlinelearning.repository.RoleRepository;
import com.example.onlinelearning.repository.UserRepository;
import com.example.onlinelearning.security.MyUserDetail;
import com.example.onlinelearning.service.UserService;
import com.example.onlinelearning.entity.Role;
import com.example.onlinelearning.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

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
        service.saveUserWithDefaultRole(user);
        return "login";
    }

    @GetMapping("/user_home")
    public String viewUserHome(@AuthenticationPrincipal MyUserDetail userDetail, Model model){
        model.addAttribute("user",userDetail);
        return "/user_home";
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

    //Delete user
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable (value = "id") int id) {
        this.service.deleteById(id);
        return "redirect:/admin_home";
    }

    //Save change for user (admin)
    @PostMapping("/addUser")
    public String addUser(User user){
        service.saveUserWithDefaultRole(user);
        return "redirect:/admin_home";
    }

    //Update user
    @GetMapping("/edit/{id}")
    public String viewUserEdit(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
        User user = service.getUserById(id);
        List<Role> listRoles = service.getRoles();

        model.addAttribute("userDetail", user);
        model.addAttribute("listRoles", listRoles);
        return "Admin_user_edit";
    }

    @GetMapping("/details/{id}")
    public String showDetails(@PathVariable("id") Integer id, Model model) {
        User user = service.getUserById(id);
        model.addAttribute("userdt", user);
        return "Admin_user_details";
    }

    @PostMapping("/update/{id}")
    public String saveUpdate(@PathVariable("id") int id, User user) {

        service.updateUser(id, user);
        return "redirect:/admin_home";
    }
}
