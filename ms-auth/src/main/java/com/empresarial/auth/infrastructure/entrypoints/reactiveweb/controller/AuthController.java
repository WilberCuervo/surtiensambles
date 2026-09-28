package com.empresarial.auth.infrastructure.entrypoints.reactiveweb.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.empresarial.auth.domain.model.Usuario;
import com.empresarial.auth.domain.usecase.usuario.RegistrarUsuarioUseCase;
import com.empresarial.auth.infrastructure.entrypoints.reactiveweb.dto.AuthResponse;
import com.empresarial.auth.infrastructure.entrypoints.reactiveweb.dto.LoginRequest;
import com.empresarial.auth.infrastructure.entrypoints.reactiveweb.dto.UsuarioRequest;
import com.empresarial.auth.infrastructure.entrypoints.reactiveweb.dto.UsuarioResponse;
import com.empresarial.auth.infrastructure.helpers.security.AuthService;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	private final RegistrarUsuarioUseCase registrarUsuarioUseCase;
	private final AuthService authService;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          ;
	
	public AuthController(RegistrarUsuarioUseCase registrarUsuarioUseCase,
			AuthService authService) {
		
		this.registrarUsuarioUseCase = registrarUsuarioUseCase;
		this.authService = authService;
		
	}
	
	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<UsuarioResponse> register(
	        @Valid @RequestBody UsuarioRequest request) {

	    Usuario usuario = Usuario.builder()
	            .username(request.getUsername())
	            .email(request.getEmail())
	            .password(request.getPassword())
	            .build();

	    return registrarUsuarioUseCase
	            .ejecutar(usuario)
	            .map(this::toResponse);
	}
	
	@PostMapping("/login")
	public Mono<AuthResponse> login(
	        @RequestBody LoginRequest request) {

	    return authService.login(
	                    request.getUsername(),
	                    request.getPassword())
	            .map(AuthResponse::new);
	}
	
	@GetMapping("/profile")
	public Mono<String> profile() {

	    return Mono.just(
	            "Usuario autenticado");
	}
	
	 private UsuarioResponse toResponse(Usuario usuario) {

	        return UsuarioResponse.builder()
	                .id(usuario.getId())
	                .username(usuario.getUsername())
	                .email(usuario.getEmail())
	                .activo(usuario.getActivo())
	                .build();
	    }

}
