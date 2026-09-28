package com.empresarial.auth.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsuarioRol {
	
	private Long usuarioId;
    
	private Long rolId;

}
