package com.cafe.cafeconnect.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * CORS configuration:
     * - Allows all origins / headers / methods (development-friendly).
     * - If you deploy to production, tighten allowed origins and consider credentials security.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Development default: allow everything. Replace with specific origins in prod.
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization", "Content-Type"));
        config.setAllowCredentials(true); // set to false if you don't use cookies/auth credentials

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    /**
     * Security filter chain:
     * - disable CSRF (your app uses APIs; enable if using cookies + forms carefully)
     * - enable cors()
     * - permit public endpoints used by frontend/postman
     * - keep everything else authenticated/allowed as needed (currently permitAll to match your original)
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().and()
                .authorizeHttpRequests()
                // public endpoints used by frontend / postman:
                .requestMatchers("/register", "/login", "/activate", "/forgot-password", "/admin/**").permitAll()
                // you can tighten this: .anyRequest().authenticated()
                .anyRequest().permitAll()
                .and()
                .httpBasic().disable(); // disable browser basic auth popup

        return http.build();
    }
}






