package com.example.onlinelearning.service;

import com.example.onlinelearning.entity.Blog;
import com.example.onlinelearning.entity.Slide;
import com.example.onlinelearning.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogService {
    @Autowired
    private BlogRepository blogRepository;

    public List<Blog> findAll() {
        return blogRepository.findAll();
    }

    public List<Blog> findByKeyword(String keyword) {
        return blogRepository.findByKeyword(keyword);
    }

    public List<Blog> findByAuthor(int id) {
        return blogRepository.findByAuthor(id);
    }

    public Blog save(Blog blog) {
        blogRepository.save(blog);
        return blog;
    }

    public List<Blog> getFeaturedBlogs() {
        return blogRepository.findAllByFeaturedEquals(1);
    }

    public Blog getBlogById(Integer id) {
        return blogRepository.getById(id);
    }

    public Page<Blog> listAll(int currentPage) {
        Pageable pageable = PageRequest.of(currentPage - 1, 5);
        return blogRepository.findAll(pageable);
    }

    public Page<Blog> getAllBlogsInCategory(Integer categoryId, int currentPage) {
        Pageable pageable = PageRequest.of(currentPage - 1, 5);
        return blogRepository.findAllByCategory_Id(categoryId, pageable);
    }

    public Page<Blog> getAllBlogsTitleContaining(String title, int currentPage) {
        Pageable pageable = PageRequest.of(currentPage - 1, 5);
        return blogRepository.findAllByTitleContaining(title, pageable);
    }


}
