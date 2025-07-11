package com.flzs.event_planner_api.security.service;

import com.flzs.event_planner_api.security.auth.*;
import com.flzs.event_planner_api.security.config.TokenJwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service for JWT token operations:
 * <p>
 * - Token generation (signing with a secret key and setting expiration).
 * - Token validation (integrity, expiration).
 * - Claim extraction (username, roles).
 * </p>
 * <p>
 * Used by: {@link JwtAuthenticationFilter}, {@link JwtValidationFilter}.
 */
@Service
public class JwtService {

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claims(claims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 2))
                .signWith(TokenJwtConfig.SECRET_KEY)
                .compact();
    }

    public String extractUsername(String token) {
        return parseToken(token).getSubject();
    }

    public List<String> extractRoles(String token) {
        return (List<String>) parseToken(token).get("authorities");
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(TokenJwtConfig.SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
