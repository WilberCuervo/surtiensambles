package com.empresarial.auth.infrastructure.helpers.security;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

public class JwtSecurityContextRepository implements ServerSecurityContextRepository {
	
	  private final JwtAuthenticationManager authenticationManager;
	
	public JwtSecurityContextRepository(JwtAuthenticationManager authenticationManager) {
		
		this.authenticationManager = authenticationManager;
		
	}

	@Override
	public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
		 return Mono.empty();
	}

	@Override
	public Mono<SecurityContext> load(ServerWebExchange exchange) {
		String authHeader = 
				exchange.getRequest()
				.getHeaders()
				.getFirst(HttpHeaders.AUTHORIZATION);
		if(authHeader == null || !authHeader.startsWith("Bearer ")){
				return Mono.empty();
			}
		
		 String token = authHeader.substring(7);

	     UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
	                        null,
	                        token);

	        return authenticationManager
	                .authenticate(auth)
	                .map(SecurityContextImpl::new);
	    }

}
