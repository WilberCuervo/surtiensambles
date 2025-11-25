package com.surtiensambles.inventario.repository;

import com.surtiensambles.inventario.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository 
        extends JpaRepository<Stock, Long>, JpaSpecificationExecutor<Stock> {
	
    Optional<Stock> findByProductoIdAndBodegaId(Long productoId, Long bodegaId);
    
    boolean existsByProductoId(Long productoId);
}
