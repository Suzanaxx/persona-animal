package com.animal.persona;

import com.animal.persona.security.FirebaseAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Omogočimo CORS za vaš front-end (http://localhost:5173)
                .cors(Customizer.withDefaults())
                // Onemogočimo CSRF, ker ga React ne pošilja samodejno
                .csrf(csrf -> csrf.disable())
                // Onemogočimo browserjev Basic‐Auth (torej ne bo več modala “Sign in”)
                .httpBasic(basic -> basic.disable())
                // Upravljanje pravic do URL-jev
                .authorizeHttpRequests(auth ->
                        auth
                                // Javne poti:
                                .requestMatchers("/api/animals/**").permitAll()
                                .requestMatchers("/api/animal_traits/**").permitAll()

                                // **TO JE KLJUČNO** – dovolite vsem dostop do /api/history/** (brez preverjanja)
                                .requestMatchers("/api/history/**").permitAll()

                                // Če imate kakšne druge klice /api/user/**, jih lahko tudi dovolite:
                                .requestMatchers("/api/user/**").permitAll()

                                // Vse ostalo pustite odprto (zaenkrat permitAll)
                                .anyRequest().permitAll()
                )
                // Če v prihodnje želite preverjati Firebase token, pustite ta filter
                .addFilterBefore(new FirebaseAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
        ;

        return http.build();
    }
}
