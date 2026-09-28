package com.empresarial.auth.infrastructure.drivenadapters.postgres.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;
@Data
@Table("usuario_rol")
public class UsuarioRolEntity {
	
	@Id
    private Long id;
	
	private Long usuarioId;
	
	private Long rolId;

}
