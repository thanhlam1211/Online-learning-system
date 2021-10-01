package com.example.onlinelearning.repository;

import com.example.onlinelearning.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Integer> {
    public List<Blog> findAllByFeaturedEquals(int featured);
}
