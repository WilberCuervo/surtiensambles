package com.empresarial.auth.domain.model.gateways;

import com.empresarial.auth.domain.model.Usuario;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UsuarioRepository {

    Mono<Usuario> save(Usuario usuario);

    Mono<Usuario> update(Usuario usuario);

    Mono<Void> delete(Long id);

    Mono<Usuario> findById(Long id);

    Mono<Usuario> findByUsername(String username);

    Mono<Usuario> findByEmail(String email);

    Flux<Usuario> findAll();
    
    Mono<Boolean> existsByUsername(String username);

    Mono<Boolean> existsByEmail(String email);

}