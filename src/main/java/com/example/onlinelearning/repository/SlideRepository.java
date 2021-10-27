package com.example.onlinelearning.repository;

import com.example.onlinelearning.entity.Blog;
import com.example.onlinelearning.entity.Slide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface SlideRepository extends JpaRepository<Slide, Integer> {
    public List<Slide> findAllByStatusValue(String status);
    public Slide getSlideById(Integer id);

    @Query(value = "select * from slide s where s.title like %:keyword%", nativeQuery = true)
    List<Slide> findByKeyword(@Param("keyword") String keyword);
}
