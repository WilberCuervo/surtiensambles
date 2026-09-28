package com.empresarial.auth.domain.usecase.auth;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.empresarial.auth.domain.model.Usuario;
import com.empresarial.auth.domain.model.gateways.UsuarioRepository;

import reactor.core.publisher.Mono;

public class LoginUseCase {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public LoginUseCase(
            UsuarioRepository usuarioRepository,
            BCryptPasswordEncoder passwordEncoder) {

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Mono<Usuario> login(
            String username,
            String password) {

        return usuarioRepository
        		.findByUsername(username)                
                .filter(usuario ->
                        passwordEncoder.matches(
                                password,
                                usuario.getPassword()));
    }
}
