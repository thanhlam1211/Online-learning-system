package com.example.onlinelearning.repository;

import com.example.onlinelearning.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Admin
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    public User getUserByUsername(String username);
    public User getUserByEmail(String email);
    public User findByEmail(String email);

    public User findByResetPasswordToken(String token);
}
