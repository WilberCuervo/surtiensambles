package com.empresarial.auth.infrastructure.drivenadapters.postgres.adapter;

import org.springframework.stereotype.Repository;

import com.empresarial.auth.domain.model.Rol;
import com.empresarial.auth.domain.model.gateways.RolRepository;
import com.empresarial.auth.infrastructure.drivenadapters.postgres.mapper.RolMapper;
import com.empresarial.auth.infrastructure.drivenadapters.postgres.repository.RolReactiveRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Repository
public class RolRepositoryAdapter implements RolRepository {
	
	private RolReactiveRepository rolReactiveRepository;
	private RolMapper rolMapper;
	
	public RolRepositoryAdapter(RolReactiveRepository rolReactiveRepository, RolMapper rolMapper) {
		
		this.rolReactiveRepository = rolReactiveRepository;
		this.rolMapper = rolMapper;
		
	}

	@Override
	public Mono<Rol> findByNombre(String nombre) {
		
		return rolReactiveRepository
				.findByNombre(nombre)
				.map(rolMapper::toDomain);
				
	}

	@Override
	public Mono<Rol> save(Rol rol) {
		return rolReactiveRepository
				.save(rolMapper.toEntity(rol))
				.map(rolMapper::toDomain);
	}

	@Override
	public Mono<Rol> findById(Long id) {
		return rolReactiveRepository
				.findById(id)
				.map(rolMapper::toDomain);
	}

	@Override
	public Flux<Rol> findAll() {

		return rolReactiveRepository
				.findAll()
				.map(rolMapper::toDomain);
	}

	

}
