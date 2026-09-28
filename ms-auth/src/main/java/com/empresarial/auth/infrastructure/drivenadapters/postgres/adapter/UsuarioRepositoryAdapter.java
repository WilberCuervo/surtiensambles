package com.empresarial.auth.infrastructure.drivenadapters.postgres.adapter;

import org.springframework.stereotype.Repository;

import com.empresarial.auth.domain.model.Usuario;
import com.empresarial.auth.domain.model.gateways.UsuarioRepository;
import com.empresarial.auth.infrastructure.drivenadapters.postgres.mapper.UsuarioDataMapper;
import com.empresarial.auth.infrastructure.drivenadapters.postgres.repository.UsuarioReactiveRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class UsuarioRepositoryAdapter implements UsuarioRepository {

    private final UsuarioReactiveRepository repository;
    private final UsuarioDataMapper mapper;

    public UsuarioRepositoryAdapter(UsuarioReactiveRepository repository,UsuarioDataMapper mapper) {

        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Usuario> save(Usuario usuario) {

        return repository
                .save(mapper.toEntity(usuario))
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Usuario> findById(Long id) {

        return repository
                .findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Usuario> findByUsername(String username) {

        return repository
                .findByUsername(username)
                .map(mapper::toDomain);
    }

    @Override
    public Flux<Usuario> findAll() {

        return repository
                .findAll()
                .map(mapper::toDomain);
    }

	@Override
	public Mono<Usuario> update(Usuario usuario) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<Void> delete(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<Usuario> findByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<Boolean> existsByUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<Boolean> existsByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

}
