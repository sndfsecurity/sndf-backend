package com.sndf.backend.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
            .csrf(csrf -> csrf.disable())

            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            .authorizeHttpRequests(auth -> auth

            	    // ✅ PUBLIC LOGIN
            	    .requestMatchers("/auth/**").permitAll()

            	    // ✅ PUBLIC ENQUIRY SUBMIT ONLY
            	    .requestMatchers(HttpMethod.POST, "/api/enquiry").permitAll()

            	    // ✅ ALL GET ENQUIRY APIs PROTECTED
            	    .requestMatchers(HttpMethod.GET, "/api/enquiry/**").authenticated()

            	    // ✅ UPDATE / DELETE ALSO PROTECTED
            	    .requestMatchers("/api/enquiry/**").authenticated()

            	    // ✅ DASHBOARD PROTECTED
            	    .requestMatchers("/api/admin/**").authenticated()

            	    // ✅ EVERYTHING ELSE PROTECTED
            	    .anyRequest().authenticated()
            	)
            .httpBasic(httpBasic -> httpBasic.disable())

            .formLogin(form -> form.disable());

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