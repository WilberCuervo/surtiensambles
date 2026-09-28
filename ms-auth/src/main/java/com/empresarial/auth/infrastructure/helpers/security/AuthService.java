package com.empresarial.auth.infrastructure.helpers.security;

import org.springframework.stereotype.Service;

import com.empresarial.auth.domain.usecase.auth.LoginUseCase;
import com.empresarial.auth.domain.usecase.usuario.ObtenerRolesUsuarioUseCase;

import reactor.core.publisher.Mono;

@Service
public class AuthService {

    private final LoginUseCase loginUseCase;
    private final JwtService jwtService;
    private final ObtenerRolesUsuarioUseCase obtenerRolesUsuarioUseCase;

    public AuthService(
            LoginUseCase loginUseCase,
            JwtService jwtService,
            ObtenerRolesUsuarioUseCase obtenerRolesUsuarioUseCase) {

        this.loginUseCase = loginUseCase;
        this.jwtService = jwtService;
        this.obtenerRolesUsuarioUseCase = obtenerRolesUsuarioUseCase;
    }

    public Mono<String> login(
            String username,
            String password) {

        return loginUseCase
                .login(username, password)
                .flatMap(usuario ->

                obtenerRolesUsuarioUseCase
                    .ejecutar(usuario.getId())

                    .map(roles ->

                        jwtService.generateToken(
                                usuario.getUsername(),
                                roles
                        )
                    )
            );
    }
}
