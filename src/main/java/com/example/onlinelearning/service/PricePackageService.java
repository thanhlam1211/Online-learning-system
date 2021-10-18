package com.example.onlinelearning.service;

import com.example.onlinelearning.entity.Course;
import com.example.onlinelearning.entity.PricePackage;
import com.example.onlinelearning.repository.CourseRepository;
import com.example.onlinelearning.repository.PricePackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PricePackageService {
    @Autowired
    private PricePackageRepository pricePackageRepository;
    @Autowired
    private CourseRepository courseRepository;

    public void savePricePackage(PricePackage pricePackage){
         pricePackageRepository.save(pricePackage);
    }

    public void saveUpdatePricePackage(PricePackage pricePackage) {
        pricePackageRepository.updatePricePackage(pricePackage.getDiscount(),
                pricePackage.getDuraion(),
                pricePackage.getListPrice(),
                pricePackage.getName(),
                pricePackage.getSalePrice(),
                pricePackage.getText(),
                pricePackage.getStatus().getId(),
                pricePackage.getId()
                );
    }

    public void activePricePackage(Integer pricePackageId, Integer courseId){
        PricePackage pricePackage = pricePackageRepository.getById(pricePackageId);
        Course course =  courseRepository.getById(courseId);
        if(pricePackage.getCourseList().contains(course)){
//            pricePackage.getCourseList().remove(course);
            course.getPricePackageList().remove(pricePackage);
//            pricePackageRepository.saveAndFlush(pricePackage);
            courseRepository.save(course);
        }else{
//            pricePackage.getCourseList().add(course);
            course.getPricePackageList().add(pricePackage);
//            pricePackageRepository.saveAndFlush(pricePackage);
            courseRepository.save(course);
        }
    }
}
