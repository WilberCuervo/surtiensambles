package com.docelectronico.security.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.docelectronico.security.dao.SecurityRepository;
import com.docelectronico.security.entity.UserEntity;
import com.docelectronico.security.service.UserService;
@Service
public class UserServiceImple implements UserService {
	
	@Autowired
	private SecurityRepository securityRepository;

	@Override
	public UserEntity getUserEntiry(String userName) {
		
		return securityRepository.findByUsuario(userName).get();
	}

}
