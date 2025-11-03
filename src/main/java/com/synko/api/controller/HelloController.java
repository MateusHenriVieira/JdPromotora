package com.synko.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map; // Importe a classe Map

// @RestController diz ao Spring que esta classe vai retornar JSON
@RestController
// @RequestMapping("/api") coloca "/api" na frente de todas as rotas desta classe
@RequestMapping("/api")
public class HelloController {

    // @GetMapping("/hello") aq deve criar a rota GET /api/hello
    @GetMapping("/hello")
    public Map<String, String> hello() {
        // Retorna um objeto Map que o Spring vai converter para JSON
        return Map.of("message", "Olá do Backend Spring Boot!");
    }
}
