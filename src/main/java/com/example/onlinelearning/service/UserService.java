package com.example.onlinelearning.service;

import com.example.onlinelearning.entity.Role;
import com.example.onlinelearning.exception.UserNotFoundException;
import com.example.onlinelearning.repository.RoleRepository;
import com.example.onlinelearning.repository.UserRepository;
import com.example.onlinelearning.entity.AuthenticationProvider;
import com.example.onlinelearning.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

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
        oldUser.setRoleList(user.getRoleList());

        repository.save(oldUser);
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

    //Delete user
    public void deleteById(Integer id) {
        this.repository.deleteById(id);
    }

    //Update user
    public void update(User user) {
        repository.save(user);
    }

    //Get Roles
    public List<Role> getRoles() {
        return roleRepo.findAll();
    }

    //Get user by keyword
    public List<User> findByKeyword(String keyword) {
        return repository.findByKeyword(keyword);
    }
}
