package com.example.onlinelearning.service;

import com.example.onlinelearning.entity.Course;
import com.example.onlinelearning.repository.DashBoardRepository;
import com.example.onlinelearning.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.onlinelearning.entity.CountCourse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashBoardService {
    @Autowired
    private CourseService courseService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    DashBoardRepository dashBoardRepository;

    public List<List<Map<Object, Object>>> getCanvasjsDataList() {
        Map<Object,Object> map = null;
        List<List<Map<Object,Object>>> list = new ArrayList<List<Map<Object,Object>>>();
        List<Map<Object,Object>> dataPoints1 = new ArrayList<Map<Object,Object>>();

        List<Course> allCourse = courseService.findAll();
        int count = allCourse.size();
        // Lỗi truyền object countcourse
        List<CountCourse> list1 = dashBoardRepository.countCourseByCategory();
        for(CountCourse item : list1){
            map = new HashMap<Object,Object>();
            map.put("label",item.getCatName());
            map.put("y", item.getCatCount()/count);
            dataPoints1.add(map);
        }
        list.add(dataPoints1);
        return list;
    }


    public List<List<Map<Object, Object>>> getMapjsDataList() {
        Map<Object,Object> map = null;
        List<List<Map<Object,Object>>> list = new ArrayList<List<Map<Object,Object>>>();
        List<Map<Object,Object>> dataPoints1 = new ArrayList<Map<Object,Object>>();

        List<Course> allCourse = courseService.findAll();
        int count = allCourse.size();
        // Lỗi truyền object countcourse
        List<CountCourse> list1 = dashBoardRepository.countCourseByCategory();
        for(CountCourse item : list1){
            map = new HashMap<Object,Object>();
            map.put("label",item.getCatName());
            map.put("y", item.getCatCount()/count);
            dataPoints1.add(map);
        }
        list.add(dataPoints1);
        return list;
    }

    public List<List<Map<Object, Object>>> getPieChart() {
        Map<Object,Object> map = null;
        List<List<Map<Object,Object>>> list = new ArrayList<List<Map<Object,Object>>>();
        List<Map<Object,Object>> dataPoints1 = new ArrayList<Map<Object,Object>>();

        List<Course> allCourse = courseService.findAll();
        int count = allCourse.size();
        // Lỗi truyền object countcourse
        List<CountCourse> list1 = dashBoardRepository.countCourseByCategory();
        for(CountCourse item : list1){
            map = new HashMap<Object,Object>();
            map.put("label",item.getCatName());
            map.put("y", item.getCatCount()/count);
            dataPoints1.add(map);
        }
        list.add(dataPoints1);
        return list;
    }

    public List<List<Map<Object, Object>>> getChart() {
        Map<Object,Object> map = null;
        List<List<Map<Object,Object>>> list = new ArrayList<List<Map<Object,Object>>>();
        List<Map<Object,Object>> dataPoints1 = new ArrayList<Map<Object,Object>>();

        List<Course> allCourse = courseService.findAll();
        int count = allCourse.size();
        // Lỗi truyền object countcourse
        List<CountCourse> list1 = dashBoardRepository.countCourseByCategory();
        for(CountCourse item : list1){
            map = new HashMap<Object,Object>();
            map.put("label",item.getCatName());
            map.put("y", item.getCatCount()/count);
            dataPoints1.add(map);
        }
        list.add(dataPoints1);
        return list;
    }
}
