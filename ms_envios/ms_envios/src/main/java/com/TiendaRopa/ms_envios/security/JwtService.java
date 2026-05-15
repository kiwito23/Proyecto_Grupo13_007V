package com.TiendaRopa.ms_envios.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private static final String SECRET_KEY = "Cl0th1ngSt0r3S2cr3tK3yF0rJWT";
    private static final long EXPIRATION_TIME = 1000 * 60 * 60;

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public String obtenerUsername(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    public boolean tokenValido(String token) {
        try {
            obtenerUsername(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}