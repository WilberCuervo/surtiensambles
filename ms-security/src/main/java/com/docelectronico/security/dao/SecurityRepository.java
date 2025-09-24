package com.docelectronico.security.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.docelectronico.security.entity.UserEntity;

@Repository
public interface SecurityRepository extends JpaRepository<UserEntity, Long> {
	
	@Query(value = "select * from usuario where user_name =:username",nativeQuery= true)
	public Optional<UserEntity> findByUsuario(String username);

}
