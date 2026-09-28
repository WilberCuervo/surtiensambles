package com.empresarial.auth.infrastructure.drivenadapters.postgres.mapper;

import org.springframework.stereotype.Component;

import com.empresarial.auth.domain.model.Rol;
import com.empresarial.auth.infrastructure.drivenadapters.postgres.entity.RolEntity;
@Component
public class RolMapper {
	
	public Rol toDomain(RolEntity rolEntity) {
		if(rolEntity == null) {
			return null;
		}
		
		return Rol.builder()
				.id(rolEntity.getId())
				.nombre(rolEntity.getNombre())
				.build();
	}
	
	public RolEntity toEntity(Rol rol) {
		
		if(rol == null ) {
			return null;
		}
		
		RolEntity rolEntity = new RolEntity();
		
		rolEntity.setId(rol.getId());
		rolEntity.setNombre(rol.getNombre());	
		
		return rolEntity;
		
	}

}
