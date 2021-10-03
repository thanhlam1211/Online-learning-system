package com.example.onlinelearning.controller;

import com.example.onlinelearning.entity.User;
import com.example.onlinelearning.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Admin
 */
@Controller
public class AppController {
    @Autowired
    private UserService service;

    @GetMapping("/process_register")
    public String createNewUser(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "sign_up";
    }

    @GetMapping("/login")
    public String loginPage() {
        //prevent user return back to login page if they already login to the system
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }
        return "redirect:/";
    }

    @GetMapping("/verify")
    public String verifyAccount(@Param("code") String code, Model model) {
        boolean verified = service.verify(code);
        String pageTitle = verified ? "Verification Succeeded!" : "Verification Failed";
        model.addAttribute("pageTitle", pageTitle);
        return (verified ? "/verify_success" : "/verify_failed");
    }
}
