package com.flzs.event_planner_api.security.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.flzs.event_planner_api.model.dto.user.AuthResponseDTO;
import com.flzs.event_planner_api.model.dto.user.LoginRequestDTO;
import com.flzs.event_planner_api.security.service.JwtService;
import com.flzs.event_planner_api.security.config.TokenJwtConfig;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Map;

/**
 * Custom filter for authentication via login ({@code /auth/login} endpoint).
 * <p>
 * Generates a JWT upon successful credential validation.
 * <p>
 * Flow:
 * 1. Attempts authentication with a {@code UsernamePasswordAuthenticationToken}.
 * 2. If authentication fails, returns a 401 Unauthorized status.
 * 3. If authentication succeeds, generates a JWT and includes it in the response.
 * </p>
 */
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        setFilterProcessesUrl("/api/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        try {
            LoginRequestDTO loginRequest = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDTO.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
        } catch (IOException e) {
            throw new AuthenticationServiceException("Login failed", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException {
        UserDetails userDetails = (UserDetails) authResult.getPrincipal();
        String token = jwtService.generateToken(userDetails);

        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("ROLE_USER");


        AuthResponseDTO authResponse = new AuthResponseDTO(token, userDetails.getUsername(), role);

        response.setContentType(TokenJwtConfig.CONTENT_TYPE);
        response.getWriter().write(
                new ObjectMapper()
                        .enable(SerializationFeature.INDENT_OUTPUT)
                        .writeValueAsString(authResponse)
        );
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        Map<String, String> error = Map.of("error", "Invalid username or password");
        response.getWriter().write(new ObjectMapper().writeValueAsString(error));
    }

}
