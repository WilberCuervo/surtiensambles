package com.empresarial.auth.infrastructure.drivenadapters.postgres.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.empresarial.auth.infrastructure.drivenadapters.postgres.entity.UsuarioRolEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Repository
public interface UsuarioRolReactiveRepository extends ReactiveCrudRepository<UsuarioRolEntity, Long> {
	
	Mono<UsuarioRolEntity>findByUsuarioIdAndRolId(Long idUsuario,Long idRol);
	
	Flux<UsuarioRolEntity> findByUsuarioId(Long usuarioId);

}
