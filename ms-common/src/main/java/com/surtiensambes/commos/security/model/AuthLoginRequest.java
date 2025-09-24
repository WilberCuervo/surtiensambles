package com.surtiensambes.commos.security.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthLoginRequest {
	@NotBlank
	private String username;
	@NotBlank
	private String password;

}
