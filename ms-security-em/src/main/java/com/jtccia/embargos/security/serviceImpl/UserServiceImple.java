package com.jtccia.embargos.security.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtccia.embargos.security.clasic.dao.UsuarioClasicRepository;
import com.jtccia.embargos.security.clasic.entity.UsuarioClasic;
import com.jtccia.embargos.security.dao.UsuarioRepository;
import com.jtccia.embargos.security.entity.Usuario;
import com.jtccia.embargos.security.service.UserService;

import jakarta.transaction.Transactional;
@Service
public class UserServiceImple implements UserService {
	
	@Autowired
	private UsuarioRepository securityRepository;
	@Autowired
	private UsuarioClasicRepository securityClasecRepository;
    @Transactional
	@Override
	public Usuario getUserEntiry(String userName) {
		
		return securityRepository.findByUsuario(userName).get();
	}
	@Override
	public UsuarioClasic getUserCalsicEntry(String userName) {
		// TODO Auto-generated method stub
		return securityClasecRepository.findByUsuario(userName).get();
	}

}
