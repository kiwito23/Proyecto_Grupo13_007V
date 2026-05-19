package com.TiendaRopa.ms_resenas.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

<<<<<<< HEAD
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;
=======
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
>>>>>>> af74f876e99ec7ab3984bcb36efe9d4b91b578ec

@Service
public class JwtService {

<<<<<<< HEAD
    private static final String SECRET_KEY = "586E3272357538782F413F4428472B4B6250655368566D597133743677397A24";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenValid(String token) {
        try {
            return !isTokenExpired(token);
=======
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

    public boolean tokenExpirado(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        Date expiracion = new Date(claims.getIssuedAt().getTime() + EXPIRATION_TIME);
        return expiracion.before(new Date());
    }

    public boolean tokenValido(String token) {
        try {
            obtenerUsername(token);
            return !tokenExpirado(token);
>>>>>>> af74f876e99ec7ab3984bcb36efe9d4b91b578ec
        } catch (Exception e) {
            return false;
        }
    }
<<<<<<< HEAD

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
=======
>>>>>>> af74f876e99ec7ab3984bcb36efe9d4b91b578ec
}