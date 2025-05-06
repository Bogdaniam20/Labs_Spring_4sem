package com.springlabs.config;  // Замените на ваш пакет

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")       // Разрешить все домены (для теста)
                        .allowedMethods("*")      // Разрешить все HTTP-методы
                        .allowedHeaders("*");     // Разрешить все заголовки
            }
        };
    }
}