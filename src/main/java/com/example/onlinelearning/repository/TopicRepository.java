package com.example.onlinelearning.repository;

import com.example.onlinelearning.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Integer> {
    public List<Topic> findAllByCourse_Id(Integer courseId);
    public List<Topic> findAll();
}
