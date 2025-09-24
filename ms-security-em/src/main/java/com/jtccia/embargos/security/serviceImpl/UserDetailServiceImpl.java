package com.jtccia.embargos.security.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jtccia.embargos.commons.security.model.AuthLoginRequest;
import com.jtccia.embargos.commons.security.model.AuthResponse;
import com.jtccia.embargos.security.dao.UsuarioRepository;
import com.jtccia.embargos.security.entity.Usuario;
import com.jtccia.embargos.security.util.JwtUtils;
@Service
public class UserDetailServiceImpl implements UserDetailsService {
	@Autowired
	private UsuarioRepository userServiceRepository;
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Usuario> userEntity = userServiceRepository.findByUsuario(username);
		
		return userEntity.map(UserDetailImpl::new)
				.orElseThrow(() -> new UsernameNotFoundException("El usuario "+username+" no existe."));
	}
	
	public AuthResponse loginUser(AuthLoginRequest autLoginRequest) {
		String username = autLoginRequest.getUsername();
		String password = autLoginRequest.getPassword();
		Authentication authentication = this.autentication(username,password);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String accesToken = jwtUtils.createToken(authentication);
		AuthResponse autResponse = AuthResponse.builder()
				.userName(username)
				.jwt(accesToken)
				.message("Usuario logeado con exito")
				.status(true)
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
