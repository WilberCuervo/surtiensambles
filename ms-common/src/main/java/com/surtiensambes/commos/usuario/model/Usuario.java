package com.surtiensambes.commos.usuario.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {
	
	private Long id;
	private String UserName;
	private String password;
	private String email;
	private String name;

}
