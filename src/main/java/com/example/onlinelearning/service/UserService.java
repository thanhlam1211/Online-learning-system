package com.example.onlinelearning.service;

import com.example.onlinelearning.config.Utility;
import com.example.onlinelearning.entity.*;
import com.example.onlinelearning.exception.UserNotFoundException;
import com.example.onlinelearning.repository.RoleRepository;
import com.example.onlinelearning.repository.StatusRepository;
import com.example.onlinelearning.repository.UserCourseRepository;
import com.example.onlinelearning.repository.UserRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

/**
 * @author Admin
 */
@Service
public class UserService {
    @Autowired
    private UserRepository repository;
    @Autowired
    private RoleRepository roleRepo;
    public User getCustomerByEmail(String email) {
        return repository.getUserByEmail(email);
    }

    public User getUserByUsername(String username) {
        return repository.getUserByUsername(username);
    }

    public User getUserById(int id) {
        return repository.getUserById(id);
    }

    public void saveUserWithDefaultRole(User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        Role roleUser = roleRepo.findByName("ROLE_STUDENT");
        Status statusUser = statusRepository.findByValue("INACTIVE");
        user.setStatus(statusUser);
        user.addRole(roleUser);

        repository.save(user);
    }

    public void updateUser(int id, User user) {
        User oldUser = repository.getUserById(id);

        oldUser.setUsername(user.getUsername());
        oldUser.setFullName(user.getFullName());
        oldUser.setEmail(user.getEmail());
        oldUser.setPhone(user.getPhone());
        oldUser.setGender(user.getGender());
        oldUser.setStatus(user.getStatus());
        oldUser.setRoleList(user.getRoleList());

        repository.save(oldUser);
    }

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    public void updateUser(User user){
        repository.save(user);
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

        helper.setFrom("EdulanSupport@gmail.com",senderName);
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
        if (user != null) {
            user.setResetPasswordToken(token);
            repository.save(user);
        } else {
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
    //Get user list
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    //Update user
    public void update(User user) {
        repository.save(user);
    }

    //Get Roles
    public List<Role> getRoles() {
        return roleRepo.findAll();
    }

    //Get status
    public List<Status> getStatus() { return statusRepository.findAll(); }

    //Get user by keyword
    public List<User> findByKeyword(String keyword) {
        return repository.findByKeyword(keyword);
    }

    //Get user admin role
    public List<User> findByAdmin() {
        return repository.findByAdmin();
    }

    // Get user by role
    public List<User> getUserByRole(int role, int status){
        return  repository.findByRole(role, status);
    }
  
    public User getUserByEmail(String email) {
        if(!email.equals("")){
            return repository.getUserByEmailContaining(email);
        }
        return null;
    }
}
