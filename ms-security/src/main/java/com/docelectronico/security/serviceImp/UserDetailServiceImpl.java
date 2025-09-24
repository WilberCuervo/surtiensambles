package com.docelectronico.security.serviceImp;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.docelectronico.security.dao.SecurityRepository;
import com.docelectronico.security.entity.UserEntity;
import com.docelectronico.security.util.JwtUtils;
import com.surtiensambes.commos.security.model.AuhtResponse;
import com.surtiensambes.commos.security.model.AuthLoginRequest;
@Service
public class UserDetailServiceImpl implements UserDetailsService {
	@Autowired
	private SecurityRepository securityServiceRepository;
	
	@Autowired
	private JwtUtils jwtUtils;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<UserEntity> userEntity = securityServiceRepository.findByUsuario(username);
		
		return userEntity.map(UserDetailImpl::new).orElse(null);
	}
	
	public AuhtResponse loginUser(AuthLoginRequest autLoginRequest) {
		String username = autLoginRequest.getUsername();
		String password = autLoginRequest.getPassword();
		Authentication authentication = this.autentication(username,password);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String accesToken = jwtUtils.createToken(authentication);
		AuhtResponse autResponse = AuhtResponse.builder()
				.userName(username)
				.jwt(accesToken)
				.message("Usuario logeado con exito")
				.state(true)
				.build();
		return autResponse;
	}

	private Authentication autentication(String username, String password) {
		UserDetails userdetails = this.loadUserByUsername(username);
		if(userdetails == null) {
			throw new BadCredentialsException("Credencial invalid");			
		}
		if(!passwordEncoder.matches(password, userdetails.getPassword())){
			throw new BadCredentialsException("password invalid");
		}
		return new UsernamePasswordAuthenticationToken(username,userdetails.getPassword(),userdetails.getAuthorities());
	}
				
	

	

}
