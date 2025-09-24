package com.docelectronico.security.serviceImp;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.docelectronico.security.entity.RoleEntity;
import com.docelectronico.security.entity.UserEntity;

import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class UserDetailImpl implements UserDetails {

	private final UserEntity userEntity;
	
	private static final long serialVersionUID = 1L;
	
	UserDetailImpl (UserEntity usr){
		this.userEntity = usr;		
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		 Set<GrantedAuthority> authorities = new HashSet<>();

	        for (RoleEntity role : userEntity.getRoles()) {
	            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));

	            role.getPermisionList().forEach(permission ->
	                authorities.add(new SimpleGrantedAuthority(permission.getName()))
	            );
	        }

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
