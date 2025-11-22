package com.surtiensambles.inventario.serviceImpl;

import com.surtiensambles.inventario.entity.MovimientoInventario;
import com.surtiensambles.inventario.repository.MovimientoInventarioRepository;
import com.surtiensambles.inventario.service.MovimientoInventarioService;
import com.surtiensambles.inventario.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovimientoInventarioServiceImpl implements MovimientoInventarioService {

    private final MovimientoInventarioRepository repository;
    private final StockService stockService;

    @Override
    public MovimientoInventario registrarMovimiento(MovimientoInventario movimiento) {
        if (movimiento.getFecha() == null) {
            movimiento.setFecha(LocalDateTime.now());
        }

        MovimientoInventario nuevo = repository.save(movimiento);

        // Actualiza stock (si es TRANSFERENCIA, StockServiceImpl manejará origen+destino)
        stockService.actualizarStock(nuevo);

        return nuevo;
    }

    @Override
    public List<MovimientoInventario> listarMovimientos() {
        return repository.findAll();
    }
}
