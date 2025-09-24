package com.jtccia.embargos.security.serviceImpl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.jtccia.embargos.security.clasic.entity.UsuarioClasic;

import lombok.Builder;
import lombok.Data;

import com.jtccia.embargos.security.clasic.entity.RolClasic;
import com.jtccia.embargos.security.clasic.entity.PermisoClasic;

@Data
@Builder
public class UserClasicDetailImpl implements UserDetails {

	private static final long serialVersionUID = 1L;

    private final UsuarioClasic userEntity;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();

        for (RolClasic rol : userEntity.getRoles()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + rol.getNombre()));

            for (PermisoClasic permiso : rol.getPermisos()) {
                authorities.add(new SimpleGrantedAuthority(permiso.getId()));
            }
        }

        return authorities;
    }

    @Override
    public String getPassword() {
        return userEntity.getClave();
    }

    @Override
    public String getUsername() {
        return userEntity.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
