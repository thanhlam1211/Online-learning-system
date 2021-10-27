package com.example.onlinelearning.repository;

import com.example.onlinelearning.entity.Blog;
import com.example.onlinelearning.entity.Slide;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Integer> {
    public List<Blog> findAllByFeaturedEquals(int featured);

    public List<Blog> findAllByOrderByIdDesc();

    public Page<Blog> findAllByCategory_Id(Integer id, Pageable pageable);

    public Page<Blog> findAllByTitleContaining(String title, Pageable pageable);

    @Query(value = "select * from blog b where b.title like %:keyword%", nativeQuery = true)
    List<Blog> findByKeyword(@Param("keyword") String keyword);

    @Query(value = "select * from blog b where b.user_id = ?1", nativeQuery = true)
    List<Blog> findByAuthor(int id);
}
