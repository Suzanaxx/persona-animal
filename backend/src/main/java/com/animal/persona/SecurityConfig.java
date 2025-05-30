package com.animal.persona;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // ⇒ Omogočimo, da Spring Security upošteva CORS nastavitve iz WebConfig
                .cors(Customizer.withDefaults())
                // ⇒ Onemogočimo CSRF (ker delamo s REST klici iz Reacta)
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Javne poti:
                        .requestMatchers("/api/animals/**").permitAll()
                        .requestMatchers("/api/animal_traits/**").permitAll()
                        .requestMatchers("/api/history/self-assessment").permitAll()
                        .requestMatchers("/api/history/self-assessments").permitAll()
                        // Vse ostale poti naj bodo prav tako javne (zdej med razvojem ni več omejitev)
                        .anyRequest().permitAll()
                )
                // Ne uporabljamo dodatne avtentikacije (za zdaj)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
