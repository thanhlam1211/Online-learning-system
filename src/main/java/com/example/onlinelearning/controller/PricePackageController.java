package com.example.onlinelearning.controller;

import com.example.onlinelearning.entity.PricePackage;
import com.example.onlinelearning.repository.PricePackageRepository;
import com.example.onlinelearning.repository.StatusRepository;
import com.example.onlinelearning.service.PricePackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Admin
 */
@Controller
public class PricePackageController {
    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private PricePackageService pricePackageService;

    @Autowired
    private PricePackageRepository pricePackageRepository;

    //--------Price Package--------
    @GetMapping("/add_pricePackage/{currentCourseId}")
    public ModelAndView addPricePackage(@PathVariable(name = "currentCourseId") Integer currentCourseId) {
        ModelAndView modelAndView = new ModelAndView("add_price_package");
        PricePackage pricePackage = new PricePackage();
        modelAndView.addObject("pricePackage", pricePackage);
        modelAndView.addObject("currentCourseId", currentCourseId);
        return modelAndView;
    }
    @GetMapping("/update_pricePackage/{packageId}/{currentCourseId}")
    public ModelAndView updatePricePackage(@PathVariable(name = "packageId") Integer packageId,
                                           @PathVariable(name = "currentCourseId") Integer currentCourseId) {
        ModelAndView modelAndView = new ModelAndView("update_price_package");
        PricePackage pricePackage = pricePackageRepository.getById(packageId);
        modelAndView.addObject("currentCourseId", currentCourseId);
        modelAndView.addObject("pricePackage", pricePackage);
        return modelAndView;
    }
    @PostMapping("/save_pricePackage")
    public String savePricePackage(@ModelAttribute("pricePackage") PricePackage pricePackage,
                                   @ModelAttribute(name = "currentCourseId") Integer currentCourseId){
        pricePackage.setStatus(statusRepository.getById(1));
        pricePackageService.savePricePackage(pricePackage);
        return "redirect:/subject_detail/"+currentCourseId.toString();
    }
    @PostMapping("/updated_pricePackage")
    public String updatedPricePackage(@ModelAttribute("pricePackage") PricePackage pricePackage,
                                      @ModelAttribute(name = "currentCourseId") Integer currentCourseId){
        pricePackageService.saveUpdatePricePackage(pricePackage);
        return "redirect:/subject_detail/"+currentCourseId.toString();
    }
    @GetMapping("/active_pricePackage/{packageId}/{currentCourseId}")
    public String activePricePackage(@PathVariable(name = "packageId") Integer packageId,
                                     @PathVariable(name = "currentCourseId") Integer currentCourseId) {
        pricePackageService.activePricePackage(packageId,currentCourseId);
        return "redirect:/subject_detail/"+currentCourseId.toString();
    }
    //------Price Package End------
}
