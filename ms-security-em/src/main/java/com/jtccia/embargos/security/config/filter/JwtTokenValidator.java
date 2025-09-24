package com.jtccia.embargos.security.config.filter;

import java.io.IOException;
import java.util.Collection;

import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.jtccia.embargos.security.util.JwtUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtTokenValidator extends OncePerRequestFilter{
	
	private JwtUtils jwtUtils;
	
	public JwtTokenValidator(JwtUtils jwtUtils) {
		
		this.jwtUtils = jwtUtils;
	}



	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull FilterChain filterChain)
			throws ServletException, IOException {
		//obtenemos del request el header la autorizacion
		String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);
		//validamos que no sea nulo el token
		if(jwtToken!=null) {
			//extraemos el token sin el bearer,
			jwtToken = jwtToken.substring(7);
			//validamos el token y decodificamos el token
			DecodedJWT decodedJWT = jwtUtils.validateToken(jwtToken);
			//Extraemos el usuario del token
			String userName = jwtUtils.extractUsername(decodedJWT);
			//extraemos el claim expecifico
			String stringauthorities = jwtUtils.getSpecificClaim(decodedJWT, "authorities").asString();
			//devolvemos una lista de las authorities
			Collection<? extends GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(stringauthorities);
			//llamamos al contexto
			SecurityContext contex = SecurityContextHolder.getContext();
			//con el usuario y la lista de authorities creamos el objeto authentication
			Authentication authentication = new UsernamePasswordAuthenticationToken(userName,null ,authorities);
			//seteamos en el contexto el objeto autentication
			contex.setAuthentication(authentication);
			//seteamos el contexto al security holder
			SecurityContextHolder.setContext(contex);
		   
		}
		
		filterChain.doFilter(request, response);
		
	}

}
