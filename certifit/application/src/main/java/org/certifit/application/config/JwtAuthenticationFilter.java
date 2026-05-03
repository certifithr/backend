package org.certifit.application.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.certifit.db.entity.enums.UserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final SecretKey secretKey;

    public JwtAuthenticationFilter(@Value("${app.jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    private SecretKey getSecretKey() {
        return secretKey;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                Claims claims = Jwts.parser()
                        .verifyWith(getSecretKey())
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();

                UUID userId = UUID.fromString(claims.getSubject());
                String email = claims.get("email", String.class);
                String role = claims.get("role", String.class);

                UserPrincipal principal = new UserPrincipal(userId, email, role);
                List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(principal, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("Authenticated user: {}", userId);

            } catch (Exception e) {
                log.warn("Invalid JWT token: {}", e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }

    public static class UserPrincipal {
        private final UUID id;
        private final String email;
        private final String role;

        public UserPrincipal(UUID id, String email, String role) {
            this.id = id;
            this.email = email;
            this.role = role;
        }

        public UUID getId() {
            return id;
        }

        public String getEmail() {
            return email;
        }

        public String getRole() {
            return role;
        }
    }
}
