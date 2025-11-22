package com.surtiensambles.inventario.repository;

import com.surtiensambles.inventario.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    /**
     * Busca el stock de un producto específico en una bodega específica.
     *
     * @param productoId ID del producto
     * @param bodegaId ID de la bodega
     * @return Optional con el Stock si existe
     */
    Optional<Stock> findByProductoIdAndBodegaId(Long productoId, Long bodegaId);
}
