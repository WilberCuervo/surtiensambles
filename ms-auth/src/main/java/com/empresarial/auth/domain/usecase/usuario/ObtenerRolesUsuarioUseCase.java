package com.empresarial.auth.domain.usecase.usuario;

import java.util.List;

import com.empresarial.auth.domain.model.gateways.RolRepository;
import com.empresarial.auth.domain.model.gateways.UsuarioRolRepository;

import reactor.core.publisher.Mono;

public class ObtenerRolesUsuarioUseCase {

	  private final UsuarioRolRepository usuarioRolRepository;
	    private final RolRepository rolRepository;

	    public ObtenerRolesUsuarioUseCase(
	            UsuarioRolRepository usuarioRolRepository,
	            RolRepository rolRepository) {

	        this.usuarioRolRepository = usuarioRolRepository;
	        this.rolRepository = rolRepository;
	    }

	    public Mono<List<String>> ejecutar(Long usuarioId) {

	        return usuarioRolRepository
	                .findByUsuarioId(usuarioId)

	                .flatMap(usuarioRol ->
	                        rolRepository.findById(
	                                usuarioRol.getRolId()))

	                .map(rol -> rol.getNombre())

	                .collectList();
	    }
}
