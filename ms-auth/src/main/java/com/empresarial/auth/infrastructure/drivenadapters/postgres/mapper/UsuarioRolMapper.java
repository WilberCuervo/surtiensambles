package com.empresarial.auth.infrastructure.drivenadapters.postgres.mapper;

import org.springframework.stereotype.Component;

import com.empresarial.auth.domain.model.UsuarioRol;
import com.empresarial.auth.infrastructure.drivenadapters.postgres.entity.UsuarioRolEntity;
@Component
public class UsuarioRolMapper {
	
	public UsuarioRol  toDomain(UsuarioRolEntity usuarioRolEntity) {
		
		if(usuarioRolEntity == null) {
			return null;
		}
		
		return UsuarioRol.builder()
				.usuarioId(usuarioRolEntity.getUsuarioId())
				.rolId(usuarioRolEntity.getRolId())
				.build();
	}
	
	public UsuarioRolEntity toEntity(UsuarioRol usuarioRol) {
		
		if(usuarioRol == null) {
			return null;
		}
		
		UsuarioRolEntity usuarioRolEntity = new UsuarioRolEntity();
		usuarioRolEntity.setRolId(usuarioRol.getRolId());
		usuarioRolEntity.setUsuarioId(usuarioRol.getUsuarioId());
		
		return usuarioRolEntity;
	}

}
