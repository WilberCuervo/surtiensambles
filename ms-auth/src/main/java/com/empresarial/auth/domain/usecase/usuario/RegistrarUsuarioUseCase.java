package com.empresarial.auth.domain.usecase.usuario;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.empresarial.auth.domain.exception.UsuarioYaExisteException;
import com.empresarial.auth.domain.model.Usuario;
import com.empresarial.auth.domain.model.gateways.UsuarioRepository;
import com.empresarial.auth.domain.service.UsuarioRolService;

import reactor.core.publisher.Mono;

public class RegistrarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UsuarioRolService usuarioRolService;

    public RegistrarUsuarioUseCase(
            UsuarioRepository usuarioRepository,
            BCryptPasswordEncoder passwordEncoder,
            UsuarioRolService usuarioRolService) {

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioRolService = usuarioRolService;
    }

    /**
     * Registra un nuevo usuario.
     */
    public Mono<Usuario> ejecutar(Usuario usuario) {

        return validarUsuario(usuario)
                .then(prepararUsuario(usuario))
                .flatMap(this::guardarUsuario)
                .flatMap(usuarioRolService::asignarRolPorDefecto);

    }

    /**
     * Valida las reglas de negocio antes del registro.
     */
    private Mono<Void> validarUsuario(Usuario usuario) {

        return usuarioRepository
                .existsByUsername(usuario.getUsername())
                .flatMap(existe -> {

                    if (Boolean.TRUE.equals(existe)) {
                        return Mono.error(
                                new UsuarioYaExisteException(
                                        usuario.getUsername()));
                    }

                    return Mono.empty();
                });

    }

    /**
     * Aplica reglas iniciales al usuario.
     */
    private Mono<Usuario> prepararUsuario(Usuario usuario) {

    	 return Mono.just(
    	            Usuario.builder()
    	                    .id(usuario.getId())
    	                    .username(usuario.getUsername())
    	                    .email(usuario.getEmail())
    	                    .password(passwordEncoder.encode(usuario.getPassword()))
    	                    .activo(Boolean.TRUE)
    	                    .build());

    }

    /**
     * Persiste el usuario.
     */
    private Mono<Usuario> guardarUsuario(Usuario usuario) {

        return usuarioRepository.save(usuario);

    }

}