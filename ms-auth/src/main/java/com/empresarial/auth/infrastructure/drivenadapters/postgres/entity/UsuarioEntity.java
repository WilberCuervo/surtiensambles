package com.empresarial.auth.infrastructure.drivenadapters.postgres.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Data
@Table("usuario")
public class UsuarioEntity {
	
    @Id
    private Long id;

    private String username;

    private String email;

    private String password;

    private Boolean activo;


}
