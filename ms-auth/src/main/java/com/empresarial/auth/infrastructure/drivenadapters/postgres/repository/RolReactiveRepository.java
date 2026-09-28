package com.empresarial.auth.infrastructure.drivenadapters.postgres.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;


import com.empresarial.auth.infrastructure.drivenadapters.postgres.entity.RolEntity;

import reactor.core.publisher.Mono;
@Repository
public interface RolReactiveRepository extends ReactiveCrudRepository<RolEntity, Long> {
  	 

	Mono<RolEntity> findByNombre(String nombre);
}
