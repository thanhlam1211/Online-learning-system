package com.example.onlinelearning.repository;

import com.example.onlinelearning.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Admin
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    public User getUserByUsername(String username);

    public User getUserById(int id);

    public User findByVerificationCode(String code);

    public User getUserByEmail(String email);

    public User findByEmail(String email);

    public User findByResetPasswordToken(String token);

    @Query(value = "select * from users u where u.full_name like %:keyword% " +
            "or u.username like %:keyword%", nativeQuery = true)
    List<User> findByKeyword(@Param("keyword") String keyword);
}
