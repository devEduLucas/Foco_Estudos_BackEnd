package com.br.eduardo.Foco_Nos_Estudos_Spring_boot.security;

import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final SecretKey chave;
    private final long expiracaoSegundos;
    private final Clock clock;

    public JwtService(@Value("${app.jwt.secret}") String segredo,
            @Value("${app.jwt.expiracao-segundos}") long expiracaoSegundos,
            Clock clock) {
        this.chave = Keys.hmacShaKeyFor(segredo.getBytes(StandardCharsets.UTF_8)); // exige >= 32 bytes
        this.expiracaoSegundos = expiracaoSegundos;
        this.clock = clock;
    }

    public String gerarToken(Long usuarioId) {
        Instant agora = clock.instant();
        return Jwts.builder()
                .subject(String.valueOf(usuarioId))
                .issuedAt(Date.from(agora))
                .expiration(Date.from(agora.plusSeconds(expiracaoSegundos)))
                .signWith(chave)
                .compact();
    }

    /** Retorna o id do usuário se o token for válido e não expirado; vazio caso contrário. */
    public Optional<Long> extrairUsuarioId(String token) {
        try {
            String sub = Jwts.parser()
                    .verifyWith(chave)
                    .clock(() -> Date.from(clock.instant()))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
            return Optional.of(Long.valueOf(sub));
        } catch (JwtException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
