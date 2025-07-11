package com.flzs.event_planner_api.security.service;

import com.flzs.event_planner_api.model.entity.User;
import com.flzs.event_planner_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * Bridges application Users with Spring Security's authentication system.
 * Loads user details from the database during JWT validation.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository repository;

    /**
     * Loads raw User entity to build Spring Security's UserDetails.
     * <p>
     * Works directly with the entity for:
     * - Only requires basic fields (username, password, role) for security operations
     * - Avoids unnecessary conversions during JWT validation
     *
     * @param username username to search (case-sensitive)
     * @return UserDetails for Spring Security
     * @throws UsernameNotFoundException if user doesn't exist
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Set.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }
}
