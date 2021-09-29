package com.example.onlinelearning.controller;

import com.example.onlinelearning.config.Utility;
import com.example.onlinelearning.entity.Status;
import com.example.onlinelearning.repository.RoleRepository;
import com.example.onlinelearning.repository.StatusRepository;
import com.example.onlinelearning.security.MyUserDetail;
import com.example.onlinelearning.service.UserService;
import com.example.onlinelearning.entity.Role;
import com.example.onlinelearning.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

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

    @GetMapping("/process_register")
    public String createNewUser(Model model){
        User user = new User();
        model.addAttribute("user",user);
        return "sign_up";
    }

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
    public String viewUserHome(@AuthenticationPrincipal MyUserDetail userDetail, Model model){
        User user = userDetail.getUser();
        model.addAttribute("user", user);
        return "user_home";
    }

    @GetMapping("/user_home/update")
    public String viewUserEdit(@AuthenticationPrincipal MyUserDetail userDetail, Model model){
        User user = userDetail.getUser();
        model.addAttribute("user", user);
        return "user_update";
    }

    @PostMapping("/user_home/update/{id}")
    public String userUpdate(@AuthenticationPrincipal MyUserDetail userDetail
            , @ModelAttribute("user") User user
            , Model model){

        User existUser = userDetail.getUser();
        existUser.setFullName(user.getFullName());
        existUser.setGender(user.getGender());
        existUser.setPhone(user.getPhone());
        //save user
        service.updateUser(existUser);
        return "redirect:/user_home";
    }

    @GetMapping("/login")
    public String loginPage(){
        //prevent user return back to login page if they already login to the system
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication instanceof AnonymousAuthenticationToken) {
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
