package com.example.onlinelearning.service;

import com.example.onlinelearning.entity.Topic;
import com.example.onlinelearning.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;
    public List<Topic> findAll(){
        return topicRepository.findAll();
    }

    public Topic getById(Integer id){return  topicRepository.getById(id);}
}
