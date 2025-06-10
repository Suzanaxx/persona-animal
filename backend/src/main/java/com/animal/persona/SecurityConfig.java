package com.animal.persona;

import com.animal.persona.security.FirebaseAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;

@Configuration
public class SecurityConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:5173");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(corsFilter(), UsernamePasswordAuthenticationFilter.class)
                .csrf(csrf -> csrf.disable())
                .httpBasic(basic -> basic.disable())
                .authorizeHttpRequests(auth -> auth
                        // Public read access
                        .requestMatchers(HttpMethod.GET, "/api/animals/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/animal_traits/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/history/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/traits/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/user/**").permitAll()

                        // Authenticated write access for categories and animals
                        .requestMatchers(HttpMethod.POST, "/api/categories/**").authenticated()
                        .requestMatchers(HttpMethod.PUT,  "/api/categories/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/categories/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/animals/**").authenticated()
                        .requestMatchers(HttpMethod.PUT,  "/api/animals/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/animals/**").authenticated()

                        // All other requests allowed for now
                        .anyRequest().permitAll()
                )
                .addFilterBefore(new FirebaseAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}