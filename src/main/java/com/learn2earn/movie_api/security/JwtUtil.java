package com.learn2earn.movie_api.security;



import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {


    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}") // i millisekunder, t.ex. 3600000 = 1h
    private long expiration;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // -----------------------------
    // GENERATE TOKEN
    // -----------------------------
    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // -----------------------------
    // EXTRACT USERNAME
    // -----------------------------
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // -----------------------------
    // VALIDATE TOKEN
    // -----------------------------
    public boolean validateToken(String token) {
        try {
            Claims claims = extractAllClaims(token);

            // kontrollera expiration
            return !claims.getExpiration().before(new Date());

        } catch (Exception e) {
            return false;
        }
    }

    // -----------------------------
    // INTERNAL: PARSE CLAIMS
    // -----------------------------
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

