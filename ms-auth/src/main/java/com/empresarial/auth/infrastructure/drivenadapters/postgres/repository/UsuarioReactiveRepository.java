package com.empresarial.auth.infrastructure.drivenadapters.postgres.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.empresarial.auth.infrastructure.drivenadapters.postgres.entity.UsuarioEntity;

import reactor.core.publisher.Mono;
@Repository
public interface UsuarioReactiveRepository extends ReactiveCrudRepository<UsuarioEntity, Long> {

	Mono<UsuarioEntity> findByUsername(String username);

    Mono<UsuarioEntity> findByEmail(String email);

    Mono<Boolean> existsByUsername(String username);

    Mono<Boolean> existsByEmail(String email);

}