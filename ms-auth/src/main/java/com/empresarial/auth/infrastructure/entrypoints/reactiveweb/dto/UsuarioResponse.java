package com.empresarial.auth.infrastructure.entrypoints.reactiveweb.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsuarioResponse {

    private Long id;

    private String username;

    private String email;

    private Boolean activo;

}
