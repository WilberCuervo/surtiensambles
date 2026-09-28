package com.empresarial.auth.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Rol {

	 private Long id;
	 
	 private String nombre;
}
