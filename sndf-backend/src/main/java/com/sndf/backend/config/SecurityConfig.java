package com.sndf.backend.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http

            // ✅ ENABLE CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // ❌ DISABLE CSRF
            .csrf(csrf -> csrf.disable())

            // ✅ AUTHORIZATION RULES
            .authorizeHttpRequests(auth -> auth

                // LOGIN APIs
                .requestMatchers("/auth/**").permitAll()

                // PUBLIC ENQUIRY FORM
                .requestMatchers("/api/enquiry").permitAll()

                // ADMIN APIs
                .requestMatchers("/api/enquiry/**").authenticated()

                // BLOCK EVERYTHING ELSE
                .anyRequest().denyAll()
            )

            // ✅ JWT STATELESS SESSION
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // ❌ DISABLE DEFAULT LOGIN
            .httpBasic(httpBasic -> httpBasic.disable())
            .formLogin(form -> form.disable());

        // ✅ JWT FILTER
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 🔐 PASSWORD ENCODER
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 🌐 CORS CONFIG
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        // ✅ FRONTEND DOMAINS
        config.setAllowedOrigins(Arrays.asList(
            "http://localhost:5173",
            "https://www.sndfndf.com",
            "https://sndfndf.com"
        ));

        // ✅ METHODS
        config.setAllowedMethods(Arrays.asList(
            "GET",
            "POST",
            "PUT",
            "DELETE",
            "OPTIONS"
        ));

        // ✅ HEADERS
        config.setAllowedHeaders(Arrays.asList("*"));

        // ✅ EXPOSE HEADERS
        config.setExposedHeaders(Arrays.asList("Authorization"));

        // ✅ ALLOW COOKIES / TOKENS
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);

        return source;
    }
}