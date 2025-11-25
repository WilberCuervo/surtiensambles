package com.surtiensambles.inventario.repository;

import com.surtiensambles.inventario.entity.EstadoReserva;
import com.surtiensambles.inventario.entity.ReservaInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaInventarioRepository extends JpaRepository<ReservaInventario, Long> {
	
    Optional<ReservaInventario> findByReferenciaReserva(String referencia);
    
    // Buscar todas las activas de la empresa 
    List<ReservaInventario> findByEstado(EstadoReserva estado);
    
    // Buscar activas de una bodega específica
    List<ReservaInventario> findByEstadoAndBodegaId(EstadoReserva estado, Long bodegaId);
}