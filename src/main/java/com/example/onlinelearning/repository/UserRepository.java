package com.example.onlinelearning.repository;

import com.example.onlinelearning.entity.CountCourse;
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

    // Get user by roles
    public List<User> getUserByRoleListEqualsAndStatus (int role, int Status);

    @Query(value = "SELECT * from users inner join user_role on users.id = user_role.user_id " +
            "WHERE role_id = ?1 and status_id = 1", nativeQuery = true)
    List<User> findByRole(int role_id, int status);

    @Query(value = "select * from users u where u.full_name like %:keyword% " +
            "or u.username like %:keyword%", nativeQuery = true)
    List<User> findByKeyword(@Param("keyword") String keyword);

    @Query(value = "SELECT t1.id, t1.[value], (t2.num_course) FROM category t1 LEFT OUTER JOIN (SELECT category_id, COUNT(category_id) AS num_course FROM course GROUP BY category_id) t2 ON t1.id = t2.category_id", nativeQuery = true)
    List<CountCourse> countCourseByCategory();
}
