package com.empresarial.auth.domain.service;

import com.empresarial.auth.domain.constants.RolesConstants;
import com.empresarial.auth.domain.exception.RolNoEncontradoException;
import com.empresarial.auth.domain.model.Rol;
import com.empresarial.auth.domain.model.Usuario;
import com.empresarial.auth.domain.model.UsuarioRol;
import com.empresarial.auth.domain.model.gateways.RolRepository;
import com.empresarial.auth.domain.model.gateways.UsuarioRolRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class UsuarioRolService {

    private final RolRepository rolRepository;
    private final UsuarioRolRepository usuarioRolRepository;

    public UsuarioRolService(
            RolRepository rolRepository,
            UsuarioRolRepository usuarioRolRepository) {

        this.rolRepository = rolRepository;
        this.usuarioRolRepository = usuarioRolRepository;
    }

    /**
     * Asigna el rol USER por defecto a un usuario recién creado.
     */
    public Mono<Usuario> asignarRolPorDefecto(Usuario usuario) {

        return asignarRol(usuario, RolesConstants.USER);

    }

    /**
     * Asigna un rol a un usuario.
     */
    public Mono<Usuario> asignarRol(
            Usuario usuario,
            String nombreRol) {

        return obtenerRol(nombreRol)

                .flatMap(rol ->

                        usuarioRolRepository.save(

                                UsuarioRol.builder()
                                        .usuarioId(usuario.getId())
                                        .rolId(rol.getId())
                                        .build())

                                .thenReturn(usuario));

    }

    /**
     * Obtiene todos los roles de un usuario.
     */
    public Flux<UsuarioRol> obtenerRoles(Long usuarioId) {

        return usuarioRolRepository.findByUsuarioId(usuarioId);

    }

    /**
     * Valida y obtiene un rol.
     */
    private Mono<Rol> obtenerRol(String nombreRol) {

        return rolRepository

                .findByNombre(nombreRol)

                .switchIfEmpty(
                        Mono.error(
                                new RolNoEncontradoException(nombreRol)));

    }

}