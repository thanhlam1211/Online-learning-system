package com.example.onlinelearning.controller;

import com.example.onlinelearning.entity.Slide;
import com.example.onlinelearning.entity.Status;
import com.example.onlinelearning.repository.SlideRepository;
import com.example.onlinelearning.repository.StatusRepository;
import com.example.onlinelearning.service.SlideService;
import jdk.internal.util.xml.impl.Input;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Locale;

@Controller
public class SlideController {
    @Autowired
    private SlideService slideService;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private SlideRepository slideRepository;

    @GetMapping("/slide")
    public String slideList(Model model) {
        List<Status> statusList = statusRepository.findAll();
        Slide slide = new Slide();

        model.addAttribute("statusList", statusList);
        model.addAttribute("slide", slide);
        model.addAttribute("slideList", slideService.getAllSlide());
        return "slide";
    }

    @PostMapping("/addSlide")
    public String saveSlide(@ModelAttribute(name = "slide") Slide slide,
                            @RequestParam("fileImage") MultipartFile multipartFile) throws IOException {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        slide.setImageUrl(fileName);
        Slide savedSlide = slideService.save(slide);

        String uploadDir = "./slide-images/" + savedSlide.getId();

        Path uploadPath = Paths.get(uploadDir);

        if(!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath ,StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOException("Could not save uploaded file: " + fileName);
        }

        return "redirect:/slide";
    }
}
