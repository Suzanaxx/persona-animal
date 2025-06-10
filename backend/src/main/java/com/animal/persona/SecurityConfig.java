package com.animal.persona;

import com.animal.persona.security.FirebaseAuthenticationFilter;
import com.animal.persona.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of("http://localhost:5173", "https://backend-wqgy.onrender.com")); // Dodan Renderjev URL
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setMaxAge(3600L);
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, UserService userService) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .httpBasic(basic -> basic.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/api/animals/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/animal_traits/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/history/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/traits/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/user/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/categories/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/categories/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/categories/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/animals/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/animals/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/animals/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/history/**").authenticated()
                        .anyRequest().permitAll()
                )
                .addFilterBefore(new FirebaseAuthenticationFilter(userService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}