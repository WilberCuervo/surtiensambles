package com.empresarial.auth.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
	
    private Long id;
    private String username;
    private String email;
    private String password;
    private Boolean activo;

}
