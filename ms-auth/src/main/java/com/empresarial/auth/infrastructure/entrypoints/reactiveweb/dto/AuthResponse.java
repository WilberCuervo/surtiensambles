package com.empresarial.auth.infrastructure.entrypoints.reactiveweb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AuthResponse {
	
	private String token;

}
