package com.empresarial.auth.infrastructure.entrypoints.reactiveweb.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.empresarial.auth.domain.usecase.usuario.RegistrarUsuarioUseCase;
import com.empresarial.auth.infrastructure.entrypoints.reactiveweb.dto.UsuarioRequest;
import com.empresarial.auth.infrastructure.entrypoints.reactiveweb.dto.UsuarioResponse;
import com.empresarial.auth.infrastructure.entrypoints.reactiveweb.mapper.UsuarioWebMapper;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/users")
public class UsuarioController {

    private final RegistrarUsuarioUseCase registrarUsuarioUseCase;
    private final UsuarioWebMapper usuarioWebMapper;

    public UsuarioController(
            RegistrarUsuarioUseCase registrarUsuarioUseCase,
            UsuarioWebMapper usuarioWebMapper) {

        this.registrarUsuarioUseCase = registrarUsuarioUseCase;
        this.usuarioWebMapper = usuarioWebMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UsuarioResponse> crearUsuario(
            @Valid @RequestBody UsuarioRequest request) {

        return registrarUsuarioUseCase
                .ejecutar(usuarioWebMapper.toDomain(request))
                .map(usuarioWebMapper::toResponse);
    }

}