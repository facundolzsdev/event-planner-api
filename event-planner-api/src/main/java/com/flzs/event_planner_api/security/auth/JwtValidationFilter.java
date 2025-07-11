package com.flzs.event_planner_api.security.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flzs.event_planner_api.security.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import static com.flzs.event_planner_api.security.config.TokenJwtConfig.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Filter that validates JWT tokens on each request.
 * <p>
 * Responsibilities:
 * - Checks the {@code Authorization} header (Bearer token).
 * - Extracts and validates the token using {@link JwtService}.
 * - Sets the authentication context in Spring Security.
 * - Delegates to the next filter if the token is valid.
 * </p>
 * <p>
 * Excludes public endpoints defined in {@code PUBLIC_ENDPOINTS}.
 */
@RequiredArgsConstructor
public class JwtValidationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private static final List<String> PUBLIC_ENDPOINTS = List.of(
            "/api/users/register",
            "/api/auth/login",
            "/v3/api-docs/**",
            "/swagger-ui/**"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        if (PUBLIC_ENDPOINTS.contains(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader(HEADER_AUTHORIZATION);

        if (header == null || !header.startsWith(PREFIX_TOKEN)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.replace(PREFIX_TOKEN, "");

        try {
            Claims claims = jwtService.parseToken(token);

            String username = claims.getSubject();
            List<String> roles = (List<String>) claims.get("authorities");

            List<GrantedAuthority> authorities = roles.stream()
                    .map(role -> (GrantedAuthority) () -> role)
                    .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(authToken);
            filterChain.doFilter(request, response);

        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(CONTENT_TYPE);

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid or expired token.");
            errorResponse.put("details", e.getMessage());

            response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
        }
    }
}
