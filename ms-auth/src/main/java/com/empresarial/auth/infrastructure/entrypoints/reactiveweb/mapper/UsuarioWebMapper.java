package com.empresarial.auth.infrastructure.entrypoints.reactiveweb.mapper;

import org.springframework.stereotype.Component;

import com.empresarial.auth.domain.model.Usuario;
import com.empresarial.auth.infrastructure.entrypoints.reactiveweb.dto.UsuarioRequest;
import com.empresarial.auth.infrastructure.entrypoints.reactiveweb.dto.UsuarioResponse;

@Component
public class UsuarioWebMapper {

    public Usuario toDomain(UsuarioRequest request) {

        return Usuario.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
    }

    public UsuarioResponse toResponse(Usuario usuario) {

        return UsuarioResponse.builder()
                .id(usuario.getId())
                .username(usuario.getUsername())
                .email(usuario.getEmail())
                .activo(usuario.getActivo())
                .build();
    }

}
