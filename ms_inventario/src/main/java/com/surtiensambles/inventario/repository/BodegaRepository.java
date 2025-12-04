package com.surtiensambles.inventario.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.surtiensambles.inventario.entity.Bodega;

@Repository
public interface BodegaRepository 
        extends JpaRepository<Bodega, Long>, JpaSpecificationExecutor<Bodega> {

    Optional<Bodega> findByCodigo(String codigo);
   
    boolean existsByCodigo(String codigo);
}
