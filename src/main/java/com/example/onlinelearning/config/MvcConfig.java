package com.example.onlinelearning.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Admin
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login_success").setViewName("/login_success");
        registry.addViewController("/login_error").setViewName("/login_error");
        registry.addViewController("/logout_success").setViewName("/logout_success");
    }
}
