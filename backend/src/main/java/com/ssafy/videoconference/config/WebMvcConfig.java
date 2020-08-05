package com.ssafy.videoconference.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer{
   @Override
   public void addCorsMappings(CorsRegistry registry) {
      registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedOrigins("http://127.0.0.1:8081")  
            .allowedOrigins("http://localhost:8081")
            .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS").allowedOrigins("*")
            .exposedHeaders("AccessToken", "RefreshToken");
      
   }
   
}
