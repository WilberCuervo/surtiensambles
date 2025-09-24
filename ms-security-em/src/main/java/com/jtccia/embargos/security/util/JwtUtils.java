package com.jtccia.embargos.security.util;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JwtUtils {
	@Value("${app.security.jwt.key}")
	private String privateKey;
	@Value("${app.security.jwt.user}")
	private String userGenerator;
	@Value("${app.security.jwt.timerExpired}")
	private Integer miliSegundos;
	
	public String createToken(Authentication authentication) {
		Algorithm algorithm = Algorithm.HMAC256(this.privateKey);
		String userName = authentication.getPrincipal().toString();
		String authorities = authentication.getAuthorities()
				.stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));
		
		String jwt = JWT.create()
				.withIssuer(this.userGenerator)
				.withSubject(userName)
				.withClaim("authorities", authorities)
				.withIssuedAt(new Date())
				.withExpiresAt(new Date(System.currentTimeMillis() + miliSegundos))
				.withJWTId(UUID.randomUUID().toString())
				.withNotBefore(new Date(System.currentTimeMillis()))
				.sign(algorithm);
				
				
		return jwt;
	}
	
	public DecodedJWT validateToken(String token) {
		
		try {
			Algorithm algorithm = Algorithm.HMAC256(this.privateKey);
			JWTVerifier verifier = JWT.require(algorithm)
					.withIssuer(this.userGenerator)
					.build();
			DecodedJWT decodedJWT = verifier.verify(token);
			return decodedJWT;
		} catch (JWTVerificationException e) {
			throw new JWTVerificationException("Token invalido, no Authorized");
		}
		
	}
	
	public String extractUsername(DecodedJWT decodedJWT) {
		
		return decodedJWT.getSubject().toString();
	}
	
	public Claim getSpecificClaim(DecodedJWT decodedJWT, String claimName) {
		Claim clain = decodedJWT.getClaim(claimName);
		return clain;	
	}
	
	public Map<String,Claim> extracAllClaim(DecodedJWT decodedJWT){
		 
		return decodedJWT.getClaims();
	}
	
	/*private Date getExpirationDateFromToken(String token) {
		DecodedJWT decodedJWT = validateToken(token);
		Map<String,Claim> cleam = extracAllClaim(decodedJWT);
		final Claim clea = cleam.get(decodedJWT.getExpiresAt());
		return (Date) clea;
	}
	*/
	

}
