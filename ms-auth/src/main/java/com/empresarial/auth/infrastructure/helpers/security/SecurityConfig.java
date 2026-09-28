package com.empresarial.auth.infrastructure.helpers.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {

    private final JwtService jwtService;

    public SecurityConfig(
            JwtService jwtService) {

        this.jwtService = jwtService;
    }

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(
            ServerHttpSecurity http) {

        JwtAuthenticationManager authManager =
                new JwtAuthenticationManager(jwtService);

        JwtSecurityContextRepository contextRepository =
                new JwtSecurityContextRepository(authManager);

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)

                .securityContextRepository(
                        contextRepository)

                .authorizeExchange(exchanges -> exchanges

                        .pathMatchers(
                        		"/api/auth/register",
                                "/api/auth/login"
                        		)
                        .permitAll()

                        .anyExchange()
                        
                        .authenticated())

                .build();
    }
}
