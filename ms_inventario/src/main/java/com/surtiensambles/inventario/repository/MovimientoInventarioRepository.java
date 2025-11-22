package com.surtiensambles.inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.surtiensambles.inventario.entity.MovimientoInventario;

public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Long> {
}
