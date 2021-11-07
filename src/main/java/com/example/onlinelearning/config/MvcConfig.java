package com.example.onlinelearning.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Admin
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("/index");
        registry.addViewController("/login").setViewName("/login");
    }

//    Config for save image path
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path blogUploadDir = Paths.get("./src/main/resources/static/blog-images");
        Path slideUploadDir = Paths.get("./src/main/resources/static/slide-images");
        Path avatarUploadDir = Paths.get("./src/main/resources/static/avatar");
        String blogUploadPath = blogUploadDir.toFile().getAbsolutePath();
        String slideUploadPath = slideUploadDir.toFile().getAbsolutePath();
        String avatarUploadPath = avatarUploadDir.toFile().getAbsolutePath();

        registry.addResourceHandler("/blog-images/**").addResourceLocations("file:/" + blogUploadPath + "/");
        registry.addResourceHandler("/slide-images/**").addResourceLocations("file:/" + slideUploadPath + "/");
        registry.addResourceHandler("/avatar/**").addResourceLocations("file:/" + avatarUploadPath + "/");
    }
}
