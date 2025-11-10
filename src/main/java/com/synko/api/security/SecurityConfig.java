package com.synko.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private FirebaseTokenFilter firebaseTokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Desabilita CSRF (não necessário para APIs stateless)
                .csrf(AbstractHttpConfigurer::disable)

                // 2. Desabilita o formulário de login padrão do Spring
                .formLogin(AbstractHttpConfigurer::disable)

                // 3. Define a política de sessão como STATELESS (sem sessões)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 4. Define as regras de autorização
                .authorizeHttpRequests(auth -> auth
                        // Protege TODOS os seus endpoints da API
                        .requestMatchers("/api/**").authenticated()
                        // Permite qualquer outra requisição (ex: se você tiver um "/")
                        .anyRequest().permitAll()
                )

                // 5. Adiciona nosso filtro FirebaseTokenFilter ANTES do filtro padrão do Spring
                .addFilterBefore(firebaseTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}