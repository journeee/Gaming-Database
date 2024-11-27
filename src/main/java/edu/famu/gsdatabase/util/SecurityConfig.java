package edu.famu.gsdatabase.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain FilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for development
                .cors(cors -> {}) // Enable CORS with defaults (configured separately in CorsConfig)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").permitAll() // Public endpoints
                        .anyRequest().authenticated() // All other endpoints require authentication
                );

        return http.build();
    }
}
