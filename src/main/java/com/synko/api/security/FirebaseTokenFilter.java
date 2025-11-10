package com.synko.api.security;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class FirebaseTokenFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            // Se não houver token, apenas continua. O Spring Security vai barrar
            // o acesso aos endpoints protegidos mais tarde.
            filterChain.doFilter(request, response);
            return;
        }

        String idToken = header.substring(7); // Remove o "Bearer "

        try {
            // Verifica o token com o Firebase Admin SDK (que já foi inicializado pelo FirebaseConfig)
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String uid = decodedToken.getUid();

            // Se o token for válido, nós o colocamos no Contexto de Segurança do Spring
            // Isso "autentica" o usuário para esta requisição
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    uid, // O 'principal' agora é o UID do Firebase
                    null, // Não precisamos de credenciais (senha)
                    new ArrayList<>() // Você pode adicionar 'roles' (perfis) aqui se usar Custom Claims
            );

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            logger.info("Token Firebase validado com sucesso para o UID: {}", uid);

        } catch (FirebaseAuthException e) {
            // Token inválido (expirado, assinatura errada, etc.)
            logger.warn("Falha ao validar token Firebase: {} - {}", e.getErrorCode(), e.getMessage());
            // Limpa o contexto de segurança
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}