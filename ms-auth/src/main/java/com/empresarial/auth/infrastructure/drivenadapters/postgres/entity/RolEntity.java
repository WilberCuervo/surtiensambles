package com.empresarial.auth.infrastructure.drivenadapters.postgres.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


import lombok.Data;
@Data
@Table("rol")
public class RolEntity {
	
	@Id
	private Long id;
	
	private String nombre;
	
	

}
