package com.surtiensambles.inventario.service;

import com.surtiensambles.inventario.entity.MovimientoInventario;
import java.util.List;

public interface MovimientoInventarioService {

    MovimientoInventario registrarMovimiento(MovimientoInventario movimiento);

    List<MovimientoInventario> listarMovimientos();
}
