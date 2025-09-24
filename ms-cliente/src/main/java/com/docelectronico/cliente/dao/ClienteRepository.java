package com.docelectronico.cliente.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.docelectronico.cliente.entity.Cliente;
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

}
