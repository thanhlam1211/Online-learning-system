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

        registry.addViewController("/login_success").setViewName("/login_success");
        registry.addViewController("/login_error").setViewName("/login_error");
        registry.addViewController("/logout_success").setViewName("/logout_success");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path blogUploadDir = Paths.get("./blog-images");
        Path slideUploadDir = Paths.get("./slide-images");
        String blogUploadPath = blogUploadDir.toFile().getAbsolutePath();
        String slideUploadPath = slideUploadDir.toFile().getAbsolutePath();

        registry.addResourceHandler("/blog-images/**").addResourceLocations("file:/" + blogUploadPath + "/");
        registry.addResourceHandler("/slide-images/**").addResourceLocations("file:/" + slideUploadPath + "/");
    }
}
