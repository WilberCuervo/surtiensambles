package com.jtccia.embargos.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jtccia.embargos.security.clasic.entity.UsuarioClasic;
import com.jtccia.embargos.security.entity.Usuario;
import com.jtccia.embargos.security.service.UserService;
@RestController
@RequestMapping("v1")
@PreAuthorize("denyAll()")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/user/{userName}")
	@PreAuthorize("hasAuthority('READ')")
	//@PreAuthorize("hasAuthority('READ') and hasAnyRole('ADMIN')")
	public Usuario getUser(@PathVariable String userName) {		
		return userService.getUserEntiry(userName);
	}
	
	@GetMapping("/userClasic/{userName}")
	public UsuarioClasic getUsuarioClasic(@PathVariable String userName) {
		return userService.getUserCalsicEntry(userName);
	}
	
	

}
