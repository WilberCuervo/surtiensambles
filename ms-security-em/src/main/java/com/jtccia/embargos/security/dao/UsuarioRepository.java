package com.jtccia.embargos.security.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.jtccia.embargos.security.entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	@Query(value = "select * from usuario where user_name =:username",nativeQuery= true)
	Optional<Usuario> findByUsuario(String username);
	
}
