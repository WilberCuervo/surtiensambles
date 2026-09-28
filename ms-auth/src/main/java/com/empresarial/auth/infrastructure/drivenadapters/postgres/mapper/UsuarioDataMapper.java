package com.empresarial.auth.infrastructure.drivenadapters.postgres.mapper;

import org.springframework.stereotype.Component;

import com.empresarial.auth.domain.model.Usuario;
import com.empresarial.auth.infrastructure.drivenadapters.postgres.entity.UsuarioEntity;

@Component
public class UsuarioDataMapper {

    public Usuario toDomain(UsuarioEntity entity) {

        if (entity == null) {
            return null;
        }

        return Usuario.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .activo(entity.getActivo())
                .build();
    }

    public UsuarioEntity toEntity(Usuario usuario) {

        if (usuario == null) {
            return null;
        }

        UsuarioEntity entity = new UsuarioEntity();

        entity.setId(usuario.getId());
        entity.setUsername(usuario.getUsername());
        entity.setEmail(usuario.getEmail());
        entity.setPassword(usuario.getPassword());
        entity.setActivo(usuario.getActivo());

        return entity;
    }

}
