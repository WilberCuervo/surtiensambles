package com.empresarial.auth.domain.usecase.usuario;

import com.empresarial.auth.domain.exception.UsuarioNoEncontradoException;
import com.empresarial.auth.domain.model.Usuario;
import com.empresarial.auth.domain.model.gateways.UsuarioRepository;

import reactor.core.publisher.Mono;

public class BuscarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    public BuscarUsuarioUseCase(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Mono<Usuario> ejecutar(Long id) {

        return usuarioRepository
                .findById(id)
                .switchIfEmpty(
                        Mono.error(
                                new UsuarioNoEncontradoException(id)));

    }

}