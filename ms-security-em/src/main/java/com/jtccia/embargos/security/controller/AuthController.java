package com.jtccia.embargos.security.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jtccia.embargos.commons.security.model.AuthLoginRequest;
import com.jtccia.embargos.commons.security.model.AuthResponse;
import com.jtccia.embargos.security.entity.Usuario;
import com.jtccia.embargos.security.service.UserService;
import com.jtccia.embargos.security.serviceImpl.UserClasicDetailServiceImpl;
import com.jtccia.embargos.security.serviceImpl.UserDetailServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("v1")
@PreAuthorize("permitAll()")
public class AuthController {	
	@Autowired
	private UserDetailServiceImpl userDetailsImple;
	@Autowired
	private UserClasicDetailServiceImpl userClasicDetailImpl;
	@Autowired
    @Qualifier("aesPasswordEncoder")
    private PasswordEncoder aesPasswordEncoder;
	
	@PostMapping("/auth/token")	
	public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthLoginRequest userRequest){
		
		return new ResponseEntity<>(this.userDetailsImple.loginUser(userRequest),HttpStatus.OK);
	}
	
	@PostMapping("/auth/tokenClasic")	
	public ResponseEntity<AuthResponse> loginClasic(@RequestBody @Valid AuthLoginRequest userRequest){
		
		return new ResponseEntity<>(this.userClasicDetailImpl.loginUser(userRequest),HttpStatus.OK);
	}
	
	 @PostMapping("/auth/encrypt")
	    public ResponseEntity<Map<String, String>> encryptForTest(@RequestBody Map<String, String> payload) {
	        String rawUser = payload.get("username");
	        String rawPass = payload.get("password");

	        String encUser = aesPasswordEncoder.encode(rawUser);
	        String encPass = aesPasswordEncoder.encode(rawPass);

	        return new ResponseEntity<>(
	            Map.of(
	                "encryptedUsername", encUser,
	                "encryptedPassword", encPass
	            ),
	            HttpStatus.OK
	        );
	    }

}
