package com.example.techchallenge.infrastructure.security;

import com.example.techchallenge.core.enums.UserRoles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint unauthorizedHandler;
    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(JwtAuthenticationEntryPoint unauthorizedHandler, JwtRequestFilter jwtRequestFilter) {
        this.unauthorizedHandler = unauthorizedHandler;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/register", "/api/users/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users/register-admin").hasAuthority(UserRoles.ADMIN.name())
                        .requestMatchers("/api/restaurants/**").hasAuthority(UserRoles.ADMIN.name())
                        .requestMatchers("/api/items/**").hasAuthority(UserRoles.ADMIN.name())
                        .requestMatchers("/api/users/**").hasAuthority(UserRoles.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/api/reservations").hasAuthority(UserRoles.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/api/reservations/status/**").hasAuthority(UserRoles.ADMIN.name())
                        .requestMatchers("/api/orders/**").authenticated()
                        .requestMatchers("/graphql", "/graphiql", "/graphiql/**").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}