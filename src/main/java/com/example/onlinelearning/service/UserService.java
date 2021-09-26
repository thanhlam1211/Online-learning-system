package com.example.onlinelearning.service;

import com.example.onlinelearning.exception.UserNotFoundException;
import com.example.onlinelearning.repository.StatusRepository;
import com.example.onlinelearning.repository.UserRepository;
import com.example.onlinelearning.entity.AuthenticationProvider;
import com.example.onlinelearning.entity.User;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

/**
 * @author Admin
 */
@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    public User getCustomerByEmail(String email){
        return repository.getUserByEmail(email);
    }
    public User getUserByName(String username){
        return repository.getUserByUsername(username);
    }
    public User getUserById(int id){
        return  repository.getUserById(id);
    }
    public void saveUser(User user) {
        String randomCode = RandomString.make(64);
        user.setVerificationCode(randomCode);
        repository.save(user);
    }

    public void sendVerificationEmail(User user, String siteUrl) throws MessagingException, UnsupportedEncodingException {
        String verifyUrl = siteUrl + "/verify?code=" + user.getVerificationCode();
        String subject ="Please verify your registration";
        String senderName = "Edulan team";
        String content =  "<table style=\"width: 100% !important\" >\n" +
                "            <tbody>\n" +
                "                <tr>\n" +
                "                    <td>\n" +
                "                        <div>\n" +
                "                            <h2>Hello "+ user.getFullName() + "</h2>\n" +
                "                        </div>\n" +
                "                        <div>\n" +
                "                            You recently register for your account in our system. Click on the link below to verify your account.\n" +
                "                        </div>\n" +
                "                        <br>\n" +
                "                        <a href=\"" +verifyUrl+ "\">Verify me</a>\n" +
                "                        <br>\n" +
                "\n" +
                "                        <div>\n" +
                "                            This link will expire in 1 days after this email was sent.\n" +
                "                        </div>\n" +
                "\n" +
                "                        <br>\n" +
                "                        <div>\n" +
                "                            Sincerely,\n" +
                "                            <h4>Edulan Team</h4>\n" +
                "                        </div>\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "            </tbody>\n" +
                "        </table>";
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("testsupport@gmail.com",senderName);
        helper.setTo(user.getEmail());
        helper.setSubject(subject);
        helper.setText(content, true);
        javaMailSender.send(message);
    }

    public boolean verify(String verificationCode) {
        User user = repository.findByVerificationCode(verificationCode);
        if(user == null || user.getStatus().getValue().equals("ACTIVE")) {
            return false;
        }else {
            user.setStatus(statusRepository.findByValue("ACTIVE"));
            repository.save(user);
            return true;
        }
    }

    public void updateResetPasswordToken(String token, String email) throws UserNotFoundException {
        User user = repository.findByEmail(email);
        if(user != null) {
            user.setResetPasswordToken(token);
            repository.save(user);
        }else{
            throw new UserNotFoundException("Could not find any user with " + email);
        }
    }

    //check user belong to the email or not
    public User get(String resetPasswordToken) {
        return repository.findByResetPasswordToken(resetPasswordToken);
    }

    public void updatePassword(User user, String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(password);

        user.setPassword(encodedPassword);
        user.setResetPasswordToken(null);

        repository.save(user);
    }

    public void createNewUserAfterOAuthLoginSuccess(String email, String name, AuthenticationProvider authenticationProvider) {
        User user = new User();
        user.setEmail(email);
        user.setUsername(name);
        user.setAuthenticationProvider(authenticationProvider);

        repository.save(user);
    }

    public void updateUserAfterOAuthLoginSuccess(User user, String name, AuthenticationProvider provider) {
        user.setUsername(name);
        user.setAuthenticationProvider(provider);

        repository.save(user);
    }


}
