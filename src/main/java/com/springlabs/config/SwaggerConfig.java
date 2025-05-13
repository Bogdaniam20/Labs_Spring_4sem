package com.springlabs.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springdoc.core.customizers.OperationCustomizer;

//http://localhost:8080/swagger-ui/index.html

@Configuration
@EnableWebMvc
public class SwaggerConfig implements WebMvcConfigurer {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Lab_Java(")
                        .description("Swagger")
                        .version("?")
                        .contact(new Contact()
                                .name("Bogdan_Grigorenko")
                                .url("https://www.tiktok.com/")
                                .email("fokc2022@mail.com"))
                        .license(new License()
                                .name(":) - press on me")
                                .url("https://g.co/kgs/1iegSL1")));
    }

    @Bean
    public OperationCustomizer customGlobalHeaders() {
        return (operation, handlerMethod) -> {
            return operation;
        };
    }
}
