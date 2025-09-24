package com.jtccia.embargos.security.serviceImpl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.jtccia.embargos.security.entity.Usuario;
import com.jtccia.embargos.security.entity.RoleEntity;

import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class UserDetailImpl implements UserDetails {

	private final Usuario userEntity;
	
	private static final long serialVersionUID = 1L;
	
	UserDetailImpl (Usuario usr){
		this.userEntity = usr;		
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> authorities = new HashSet<>();
		
		//userEntity.getRoles().stream()
		
		
		
		userEntity.getRoles()
		  		  .forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName())));
		userEntity.getRoles()
		          .stream()
		          .flatMap(role -> role.getPermisionList().stream())
		          .forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.getName())));
		          
		 /**
		  *  forma 1
		  */
	       /* for (RoleEntity role : userEntity.getRoles()) {
	            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));

	            role.getPermisionList().forEach(permission ->
	                authorities.add(new SimpleGrantedAuthority(permission.getName()))
	            );
	        }*/
		 /**
		  * forma 2
		  */
		 		 		   
	        return authorities;
	}

	@Override
    public String getPassword() {
        // Devuelve la contraseña de la entidad
        return userEntity.getPassword();
    }

    @Override
    public String getUsername() {
        // Devuelve el nombre de usuario de la entidad
        return userEntity.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return userEntity.isAccountNoExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return userEntity.isAccounNoLoked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return userEntity.isCredentialNoExpired();
    }

    @Override
    public boolean isEnabled() {
        return userEntity.isEnabled();
    }

}
