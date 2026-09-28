package com.empresarial.auth.domain.model.gateways;


import com.empresarial.auth.domain.model.UsuarioRol;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UsuarioRolRepository {
 
	 Mono<UsuarioRol> save(UsuarioRol usuarioRol);	 	 
	 
	 Mono<UsuarioRol>findByUsuarioIdAndRolId(Long idUsuario,Long idRol);

	 Flux<UsuarioRol> findAll();
	 
	 Flux<UsuarioRol> findByUsuarioId(Long usuarioId);
}
