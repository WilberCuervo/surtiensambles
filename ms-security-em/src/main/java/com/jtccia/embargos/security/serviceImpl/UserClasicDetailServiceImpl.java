package com.jtccia.embargos.security.serviceImpl;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import com.jtccia.embargos.security.clasic.dao.UsuarioClasicRepository;
import com.jtccia.embargos.security.clasic.entity.UsuarioClasic;
import com.jtccia.embargos.security.util.AesPasswordEncoder;
import com.jtccia.embargos.security.util.JwtUtils;

@Service("userClasicDetailService")
public class UserClasicDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioClasicRepository usuarioClasicRepository;
    
    @Autowired
	private JwtUtils jwtUtils;
	
    /**
     * Inyectamos específicamente el PasswordEncoder de AES
     */
    @Autowired
    @Qualifier("aesPasswordEncoder")
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	
        Optional<UsuarioClasic> optionalUser = usuarioClasicRepository.findByUsuario(username);

        return optionalUser
                .map(UserClasicDetailImpl::new)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario clásico no encontrado: " + username));
    }
    
    /**
     * Método para login vía controlador.  
     * Verifica credenciales, establece el contexto y devuelve AuthResponse con JWT.
     */
    public AuthResponse loginUser(AuthLoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String rawPassword = loginRequest.getPassword();
        
        Authentication auth = authenticateUser(username, rawPassword);
        SecurityContextHolder.getContext().setAuthentication(auth);
        
        String token = jwtUtils.createToken(auth);
        return AuthResponse.builder()
                .userName(username)
                .jwt(token)
                .message("Usuario logeado con éxito")
                .status(true)
                .build();
    }

    /**
     * Lógica de autenticación: carga UserDetails + comprueba password con AES
     */
    private Authentication authenticateUser(String username, String rawPassword) {
        UserDetails userDetails = loadUserByUsername(username);
        
        if (!passwordEncoder.matches(rawPassword, userDetails.getPassword())) {
            throw new BadCredentialsException("Credenciales inválidas para usuario " + username);
        }
        
        // La autenticación pasa, creamos el token de Spring Security
        return new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getAuthorities()
        );
    }
}
