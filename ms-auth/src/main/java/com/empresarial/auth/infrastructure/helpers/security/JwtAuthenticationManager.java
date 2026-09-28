package com.empresarial.auth.infrastructure.helpers.security;

import java.util.List;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import reactor.core.publisher.Mono;

public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

	private final JwtService jwtService;

	public JwtAuthenticationManager(JwtService jwtService) {

		this.jwtService = jwtService;
	}

	@Override
	public Mono<Authentication> authenticate(Authentication authentication) {

		String token = authentication.getCredentials().toString();

		if (!jwtService.validateToken(token)) {
			return Mono.empty();
		}
		
		List<String> roles = jwtService.extractRoles(token);
		
		List<GrantedAuthority> authorities =
		        roles.stream()
		             .map(role -> "ROLE_" + role)
		             .map(SimpleGrantedAuthority::new)
		             .map(GrantedAuthority.class::cast)
		             .toList();

		String username = jwtService.extractUsername(token);

		Authentication auth = new UsernamePasswordAuthenticationToken(username, null, authorities);

		return Mono.just(auth);
	}
}