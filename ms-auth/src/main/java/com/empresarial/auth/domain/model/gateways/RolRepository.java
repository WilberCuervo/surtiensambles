package com.empresarial.auth.domain.model.gateways;

import com.empresarial.auth.domain.model.Rol;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RolRepository {
	
	 Mono<Rol> findByNombre(String nombre);
	 
	 Mono<Rol> save(Rol rol);

	 Mono<Rol> findById(Long id);

	 Flux<Rol> findAll();

}
