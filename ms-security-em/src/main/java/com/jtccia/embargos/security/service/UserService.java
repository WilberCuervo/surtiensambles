package com.jtccia.embargos.security.service;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.jtccia.embargos.security.clasic.entity.UsuarioClasic;
import com.jtccia.embargos.security.entity.Usuario;

public interface UserService {
	
	public Usuario getUserEntiry(String userName);
	
	public UsuarioClasic getUserCalsicEntry(String userName);
}
