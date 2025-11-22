package com.surtiensambles.inventario.repository;

import com.surtiensambles.inventario.entity.ReservaInventario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReservaInventarioRepository extends JpaRepository<ReservaInventario, Long> {
    Optional<ReservaInventario> findByReferenciaReserva(String referencia);
}
