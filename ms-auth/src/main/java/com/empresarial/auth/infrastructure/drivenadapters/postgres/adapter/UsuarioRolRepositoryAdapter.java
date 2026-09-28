package com.empresarial.auth.infrastructure.drivenadapters.postgres.adapter;

import org.springframework.stereotype.Repository;

import com.empresarial.auth.domain.model.UsuarioRol;
import com.empresarial.auth.domain.model.gateways.UsuarioRolRepository;
import com.empresarial.auth.infrastructure.drivenadapters.postgres.mapper.UsuarioRolMapper;
import com.empresarial.auth.infrastructure.drivenadapters.postgres.repository.UsuarioRolReactiveRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Repository
public class UsuarioRolRepositoryAdapter implements UsuarioRolRepository {
	
	private UsuarioRolReactiveRepository usuarioRolReactiveRepository;
	private UsuarioRolMapper usuarioRolMapper;
	
	public UsuarioRolRepositoryAdapter(UsuarioRolReactiveRepository usuarioRolReactiveRepository, UsuarioRolMapper usuarioRolMapper) {
		this.usuarioRolReactiveRepository = usuarioRolReactiveRepository;
		this.usuarioRolMapper = usuarioRolMapper;
	}

	@Override
	public Mono<UsuarioRol> save(UsuarioRol usuarioRol) {
		
		return usuarioRolReactiveRepository
				 .save(usuarioRolMapper.toEntity(usuarioRol))
				 .map(usuarioRolMapper::toDomain);
	}	

	@Override
	public Mono<UsuarioRol> findByUsuarioIdAndRolId(Long idUsuario, Long idRol) {
		
		return usuarioRolReactiveRepository
				.findByUsuarioIdAndRolId(idUsuario, idRol)
				.map(usuarioRolMapper::toDomain);
	}

	@Override
	public Flux<UsuarioRol> findAll() {
		
		return usuarioRolReactiveRepository
				.findAll()
				.map(usuarioRolMapper::toDomain);
	}

	@Override
	public Flux<UsuarioRol> findByUsuarioId(Long usuarioId) {
		
		return usuarioRolReactiveRepository
				.findByUsuarioId(usuarioId)
				.map(usuarioRolMapper::toDomain);

	}

}
