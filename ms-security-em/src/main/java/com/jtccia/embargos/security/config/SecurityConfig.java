package com.jtccia.embargos.security.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.jtccia.embargos.security.config.filter.JwtTokenValidator;
import com.jtccia.embargos.security.serviceImpl.UserClasicDetailServiceImpl;
import com.jtccia.embargos.security.serviceImpl.UserDetailServiceImpl;
import com.jtccia.embargos.security.util.AesPasswordEncoder;
import com.jtccia.embargos.security.util.JwtUtils;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity
			    .cors(Customizer.withDefaults())
				.httpBasic(Customizer.withDefaults())
				.csrf(crf -> crf.disable())
				.sessionManagement(sesion-> sesion.sessionCreationPolicy(SessionCreationPolicy.STATELESS))	//sin estado de sesion para eso se cumple el token
				.addFilterBefore(new JwtTokenValidator(jwtUtils), BasicAuthenticationFilter.class)
				.build();
	}
	
	/*@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}*/
	
	@Bean
	public AuthenticationManager authenticationManager(
	        AuthenticationProvider authenticationProvider,
	        AuthenticationProvider otherAppAuthenticationProvider) {

	    List<AuthenticationProvider> providers = new ArrayList<>();
	    providers.add(authenticationProvider); // moderno
	    providers.add(otherAppAuthenticationProvider); // clásico

	    return new org.springframework.security.authentication.ProviderManager(providers);
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider(UserDetailServiceImpl userDetailServiceImpl) {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailServiceImpl);
		provider.setPasswordEncoder(passwordEncoder());		
		return provider;
	}
	
	@Bean
	public AuthenticationProvider otherAppAuthenticationProvider(UserClasicDetailServiceImpl uds,
			 @Qualifier("aesPasswordEncoder") PasswordEncoder encoder) {
	    DaoAuthenticationProvider p = new DaoAuthenticationProvider(uds);
	    p.setPasswordEncoder(aesPasswordEncoder());
	    return p;
	}
	
	@Bean("aesPasswordEncoder")
	public PasswordEncoder aesPasswordEncoder() {
	    return new AesPasswordEncoder();
	}
	 
	@Bean
	@Primary
	public PasswordEncoder passwordEncoder() {		
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public PasswordEncoder passwordEncoderNo() {		
		return NoOpPasswordEncoder.getInstance();
	}
	
	public static void main(String[] args) {
		System.out.println(new BCryptPasswordEncoder().encode("12345"));
		System.out.println(new AesPasswordEncoder().encode("Master01**"));
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
