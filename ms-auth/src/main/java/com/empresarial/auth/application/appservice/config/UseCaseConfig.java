package com.empresarial.auth.application.appservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.empresarial.auth.domain.model.gateways.RolRepository;
import com.empresarial.auth.domain.model.gateways.UsuarioRepository;
import com.empresarial.auth.domain.model.gateways.UsuarioRolRepository;
import com.empresarial.auth.domain.service.UsuarioRolService;
import com.empresarial.auth.domain.usecase.auth.LoginUseCase;
import com.empresarial.auth.domain.usecase.usuario.ObtenerRolesUsuarioUseCase;
import com.empresarial.auth.domain.usecase.usuario.RegistrarUsuarioUseCase;

@Configuration
public class UseCaseConfig {
	
	@Bean
	public UsuarioRolService usuarioRolService(
	        RolRepository rolRepository,
	        UsuarioRolRepository usuarioRolRepository) {

	    return new UsuarioRolService(
	            rolRepository,
	            usuarioRolRepository);
	}

	@Bean
	public RegistrarUsuarioUseCase registrarUsuarioUseCase(
	        UsuarioRepository usuarioRepository,
	        BCryptPasswordEncoder passwordEncoder,
	        UsuarioRolService usuarioRolService) {

	    return new RegistrarUsuarioUseCase(
	            usuarioRepository,
	            passwordEncoder,
	            usuarioRolService);
	}
    
    @Bean
    public LoginUseCase loginUseCase(
            UsuarioRepository usuarioRepository,
            BCryptPasswordEncoder passwordEncoder) {

        return new LoginUseCase(
                usuarioRepository,
                passwordEncoder);
    }
    
    @Bean
    public ObtenerRolesUsuarioUseCase obtenerRolesUsuarioUseCase(
            UsuarioRolRepository usuarioRolRepository,
            RolRepository rolRepository) {

        return new ObtenerRolesUsuarioUseCase(
                usuarioRolRepository,
                rolRepository);
    }

}
