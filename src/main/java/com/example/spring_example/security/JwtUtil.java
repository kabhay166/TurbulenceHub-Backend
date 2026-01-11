package com.example.spring_example.security;

import com.example.spring_example.enums.JwtTokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwttoken.secret:ZB7VI17nH6JOmp49Uhtw3gbmsz5Yu4qw}")
    private String SECRET_KEY;

    public String extractUsername(String token) {
        return extractClaim(token,Claims::getSubject);
    }

    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    public Date extractExpiration(String token) {
        return extractClaim(token,Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        return Jwts.parserBuilder()
                .setAllowedClockSkewSeconds(60)
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(JwtTokenType tokenType, String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        return createToken(tokenType,claims,username);
    }

    private String createToken(JwtTokenType tokenType ,Map<String, Object> claims, String subject) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        long now = System.currentTimeMillis();
        Date issuedAt = new Date(now);

        Date expiryAt = null;

        if(tokenType == JwtTokenType.ACCESS_TOKEN) {
           expiryAt = new Date(now + TimeUnit.MINUTES.toMillis(60));
        } else {
            expiryAt = new Date(now + TimeUnit.DAYS.toMillis(1));
        }

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(issuedAt)
                .setExpiration(expiryAt)
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token) {
        return !isTokenExpired(token);
    }


}
