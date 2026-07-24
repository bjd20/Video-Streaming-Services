package com.videostreaming.account.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("Video Streaming - Account Service")
                        .description("API Documentation for Account Service")
                        .version("1.0")
                        .contact(new Contact()
                        .name("Bhaskar Das")
                        .email("bhaskarjd20@gmail.com")
                        )
                );
    }
}
