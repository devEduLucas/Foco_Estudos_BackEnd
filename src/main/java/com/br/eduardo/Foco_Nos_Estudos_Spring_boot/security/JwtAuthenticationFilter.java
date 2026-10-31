package com.br.eduardo.Foco_Nos_Estudos_Spring_boot.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** Lê "Authorization: Bearer <jwt>" e coloca o id do usuário (Long) como principal. */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String PREFIXO = "Bearer ";

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith(PREFIXO)) {
            jwtService.extrairUsuarioId(header.substring(PREFIXO.length()))
                    .ifPresent(id -> SecurityContextHolder.getContext()
                            .setAuthentication(new UsernamePasswordAuthenticationToken(id, null, List.of())));
        }
        chain.doFilter(request, response);
    }
}
