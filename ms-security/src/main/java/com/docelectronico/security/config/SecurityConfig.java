 package com.docelectronico.security.config;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthenticatedAuthorizationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.docelectronico.security.config.filter.JwtTokenValidator;
import com.docelectronico.security.serviceImp.UserDetailServiceImpl;
import com.docelectronico.security.util.JwtUtils;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig { //<S extends Session>
	
	@Autowired
	private JwtUtils jwtUtils;

	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity
				.httpBasic(Customizer.withDefaults())
				.csrf(crf -> crf.disable())
				.sessionManagement(sesion-> sesion.sessionCreationPolicy(SessionCreationPolicy.STATELESS))	//sin estado de sesion para eso se cumple el token
				.addFilterBefore(new JwtTokenValidator(jwtUtils), BasicAuthenticationFilter.class)
				.build();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailService());
		provider.setPasswordEncoder(passwordEncoder());		
		return provider;
	}
	
	public static void main(String[] args) {
		System.out.println(new BCryptPasswordEncoder().encode("12345"));
	}
	
	 
	@Bean
	public PasswordEncoder passwordEncoder() {		
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public PasswordEncoder passwordEncoderNo() {		
		return NoOpPasswordEncoder.getInstance();
	}
	
	@Bean
	public SessionRegistry sessionRegister() {
		return new SessionRegistryImpl();
	}
	
	
	
	public AuthenticationSuccessHandler successHandler() {
		return ((request,response,Authentication) -> 
		response.sendRedirect("/v1/index"));
	}
	
	@Bean
	public UserDetailsService userDetailService() {		
		return new UserDetailServiceImpl();
		
	}
	
	
	@Bean
	public UserDetailsService userDetailServiceMemory() {
		List<UserDetails> userDetailsList = new ArrayList<>();
		
		userDetailsList.add(User.withUsername("wilber")
				.password("12345")
				.roles("ADMIN")
				.authorities("CREATE","READ")
				.build());
		
		userDetailsList.add(User.withUsername("leidy")
				.password("12345")
				.roles("ADMIN")
				.authorities("READ")
				.build());
		
		userDetailsList.add(User.withUsername("poveda")
				.password("12345")
				.roles("ADMIN")
				.authorities("READ")
				.build());
		
		return new InMemoryUserDetailsManager(userDetailsList);
	}
	
	
	

}
