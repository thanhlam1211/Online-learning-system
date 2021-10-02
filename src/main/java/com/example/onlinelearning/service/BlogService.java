package com.example.onlinelearning.service;

import com.example.onlinelearning.entity.Blog;
import com.example.onlinelearning.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogService {
    @Autowired
    private BlogRepository blogRepository;

    public List<Blog> getFeaturedBlogs() {
        return blogRepository.findAllByFeaturedEquals(1);
    }

    public Blog getBlogById(Integer id) {
        return blogRepository.getById(id);
    }
}
