package com.synko.api.config;

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
                registry.addMapping("/**") // todas as rotas
                        .allowedOriginPatterns("*") // todas as origens (mais seguro que allowedOrigins("*"))
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // todos os métodos comuns
                        .allowedHeaders("*") // todos os headers
                        .allowCredentials(true); // permite cookies/autenticação
            }
        };
    }
}
