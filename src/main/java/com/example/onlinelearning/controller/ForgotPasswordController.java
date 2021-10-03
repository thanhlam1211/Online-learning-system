package com.example.onlinelearning.controller;

import com.example.onlinelearning.config.Utility;
import com.example.onlinelearning.exception.UserNotFoundException;
import com.example.onlinelearning.service.UserService;
import com.example.onlinelearning.entity.User;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

/**
 * @author Admin
 */
@Controller
public class ForgotPasswordController {
    @Autowired
    private UserService userService;

    @Autowired
    private JavaMailSender JavaMailSender;

    @GetMapping("/forgot_password")
    public String showForgotPasswordForm(Model model) {
        model.addAttribute("pageTitle","Forgot password");
        return "forgot_password_form";
    }

    @PostMapping("/forgot_pass")
    public String processForgotPassword(HttpServletRequest request, Model model) throws MessagingException, UnsupportedEncodingException {
        String email = request.getParameter("email");
        String token = RandomString.make(45);
        try {
            userService.updateResetPasswordToken(token, email);
            //generate reset pass link
            String resetPasswordLink = Utility.getSiteURL(request) + "/reset_password?token=" + token;
            sendEmail(email, resetPasswordLink);

            model.addAttribute("message","We have sent a reset password link to your email. Please check");

        } catch (UserNotFoundException e) {
            model.addAttribute("error",e.getMessage());
        }
        model.addAttribute("pageTitle","Forgot password");
        return "forgot_password_form";
    }

    //send mail
    private void sendEmail(String email, String resetPasswordLink) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = JavaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("EdulanSupport@gmail.com","Edulan support");
        helper.setTo(email);

        String subject = "Here's the link to reset the password";
        String content =  "<table style=\"width: 100% !important\" >\n" +
                "            <tbody>\n" +
                "                <tr>\n" +
                "                    <td>\n" +
                "                        <div>\n" +
                "                            <h2>Hello</h2>\n" +
                "                        </div>\n" +
                "                        <div>\n" +
                "                            You recently took steps to reset the password for your account. Click on the link below to reset your password.\n" +
                "                        </div>\n" +
                "                        <br>\n" +
                "                        <a href=\"" +resetPasswordLink+ "\">Reset Password</a>\n" +
                "                        <br>\n" +
                "\n" +
                "                        <div>\n" +
                "                            This link will expire in 1 days after this email was sent.\n" +
                "                        </div>\n" +
                "\n" +
                "                        <br>\n" +
                "                        <div>\n" +
                "                            Sincerely,\n" +
                "                            <h4>The Team</h4>\n" +
                "                        </div>\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "            </tbody>\n" +
                "        </table>";
        helper.setSubject(subject);
        helper.setText(content, true);
        JavaMailSender.send(message);
    }

    @GetMapping("/reset_password")
    public String showResetPasswordForm(@Param(value = "token") String token, Model model){
        User user = userService.get(token);
        if(user == null) {
            model.addAttribute("title", "Reset your password");
            model.addAttribute("message", "Invalid token");
            return "message";
        }
        model.addAttribute("token",token);
        model.addAttribute("pageTitle","Reset your password");

        return "reset_password_form";
    }
    @PostMapping("/reset_password")
    public String processResetPassword(HttpServletRequest request, Model model){
        String token = request.getParameter("token");
        String password = request.getParameter("password");
        User user = userService.get(token);
        if(user == null) {
            model.addAttribute("title", "Reset your password");
            model.addAttribute("message", "Invalid token");
            return "message";
        }else{
            userService.updatePassword(user, password);
            model.addAttribute("message", "Successfully changed your password");

        }
        return "message";
    }
}
