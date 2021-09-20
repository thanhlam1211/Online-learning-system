package com.example.onlinelearning.repository;


import com.example.onlinelearning.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Admin
 */
public interface RoleRepository extends JpaRepository<Role, Integer> {
    public Role findByName(String name);
}
