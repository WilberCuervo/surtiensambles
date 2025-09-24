package com.docelectronico.security.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.docelectronico.security.entity.UserEntity;
import com.docelectronico.security.service.UserService;
import com.docelectronico.security.serviceImp.UserDetailServiceImpl;
import com.surtiensambes.commos.security.model.AuhtResponse;
import com.surtiensambes.commos.security.model.AuthLoginRequest;

import jakarta.validation.Valid;

@RestController
@RequestMapping("auth/v1")
@PreAuthorize("permitAll()")
public class AuthController {
	@Autowired
	public SessionRegistry sessionRegistry;
	@Autowired
	private UserService userServiceImpl;
	@Autowired
	private UserDetailServiceImpl userDetailsImple;
	
	//@GetMapping("/user/{userName}")
	//@PreAuthorize("hasAuthority('READ')")
	public UserEntity obtenerUsuario(@PathVariable String userName) {		
		return userServiceImpl.getUserEntiry(userName);
	}
	@PostMapping("/user/log-in")
	public ResponseEntity<AuhtResponse> login(@RequestBody @Valid AuthLoginRequest userRequest){
		
		return new ResponseEntity<>(this.userDetailsImple.loginUser(userRequest),HttpStatus.OK);
	}
	
	
	
	@GetMapping("/index")
	@PreAuthorize("permitAll()")
	public String index() {
		return "hola Mundo";
	}
	
	@GetMapping("/hello-secured")
	@PreAuthorize("hasAuthority('READ')")
	public String helloSecured() {
		return "hola autorizado por lectura";
	}
	
	@GetMapping("/hello-secured-create")
	@PreAuthorize("hasAuthority('CREATE')")
	public String index2() {
			return "hola autorizado por crear";
	}
	
	@GetMapping("/session")
	public ResponseEntity<?> getDetailSession(){
		String sessionId = "";
		User userObject = null;

		
		List<Object> session = sessionRegistry.getAllPrincipals();
		
		for(Object sessions : session) {
			if(sessions instanceof User) {
			   userObject = (User) sessions;
			}
			 List<SessionInformation> sessionInformations = sessionRegistry.getAllSessions(session, false);
			for(SessionInformation sessionInformation : sessionInformations ) {
		   		  sessionId = sessionInformation.getSessionId(); 
		   	  }
		}
		
	
		Map<String,Object> response = new HashMap<>();
		response.put("response", "hola mundo ");
		response.put("sessionId", sessionId);
		response.put("sessionUser", userObject);
		return ResponseEntity.ok(response);
	}

}
