package com.jtccia.embargos.security.clasic.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.jtccia.embargos.security.clasic.entity.UsuarioClasic;
@Repository
public interface UsuarioClasicRepository extends JpaRepository<UsuarioClasic, String> {
    @Query(value = "SELECT * FROM usuario WHERE login = :username", nativeQuery = true)
    Optional<UsuarioClasic> findByUsuario(String username);
}