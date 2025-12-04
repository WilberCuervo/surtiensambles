package com.surtiensambles.inventario.repository;

import com.surtiensambles.inventario.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository 
        extends JpaRepository<Stock, Long>, JpaSpecificationExecutor<Stock> {
	
    Optional<Stock> findByProductoIdAndBodegaId(Long productoId, Long bodegaId);
    
    boolean existsByProductoId(Long productoId);
    
    // --- Consulta de Alertas ---
    // Busca Stock donde la cantidad disponible sea menor o igual al nivel de reorden del producto.
    // Solo traemos productos ACTIVOS (para no alertar sobre cosas descontinuadas).
    @Query("SELECT s FROM Stock s WHERE s.cantidadDisponible <= s.producto.nivelReorden AND s.producto.activo = true ORDER BY s.cantidadDisponible ASC")
    List<Stock> findProductosBajosDeStock();
    
    // Opcional: Si quieres filtrar por una bodega específica
    @Query("SELECT s FROM Stock s WHERE s.bodega.id = :bodegaId AND s.cantidadDisponible <= s.producto.nivelReorden AND s.producto.activo = true")
    List<Stock> findProductosBajosDeStockPorBodega(@Param("bodegaId") Long bodegaId);
}
