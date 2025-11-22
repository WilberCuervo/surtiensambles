package com.surtiensambles.inventario.serviceImpl;

import com.surtiensambles.inventario.entity.MovimientoInventario;
import com.surtiensambles.inventario.entity.Stock;
import com.surtiensambles.inventario.repository.StockRepository;
import com.surtiensambles.inventario.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;

    @Override
    @Transactional
    public void actualizarStock(MovimientoInventario movimiento) {
        Stock stock = stockRepository.findByProductoIdAndBodegaId(
                movimiento.getProducto().getId(),
                movimiento.getBodega().getId()
        ).orElseThrow(() -> new RuntimeException("Stock no encontrado para el producto y bodega"));

        switch (movimiento.getTipo()) {
            case "ENTRADA":
            case "AJUSTE":
                stock.setCantidadDisponible(stock.getCantidadDisponible() + movimiento.getCantidad());
                break;
            case "SALIDA":
                if (stock.getCantidadDisponible() < movimiento.getCantidad()) {
                    throw new RuntimeException("No hay suficiente stock disponible");
                }
                stock.setCantidadDisponible(stock.getCantidadDisponible() - movimiento.getCantidad());
                break;
            default:
                throw new RuntimeException("Tipo de movimiento inválido: " + movimiento.getTipo());
        }

        stock.setUltimaActualizacion(LocalDateTime.now());
        stockRepository.save(stock);
    }

    @Override
    @Transactional
    public void transferir(MovimientoInventario movimiento) {
        Long bodegaDestinoId = Long.parseLong(movimiento.getReferenciaId());

        // Stock de la bodega origen
        Stock origen = stockRepository.findByProductoIdAndBodegaId(
                movimiento.getProducto().getId(),
                movimiento.getBodega().getId()
        ).orElseThrow(() -> new RuntimeException("Stock origen no encontrado"));

        // Stock de la bodega destino (si no existe, crearlo)
        Stock destino = stockRepository.findByProductoIdAndBodegaId(
                movimiento.getProducto().getId(),
                bodegaDestinoId
        ).orElseGet(() -> {
            Stock nuevo = new Stock();
            nuevo.setProducto(movimiento.getProducto());
            nuevo.setBodega(movimiento.getBodega()); 
            nuevo.setCantidadDisponible(0);
            nuevo.setCantidadReservada(0);
            return nuevo;
        });

        if (origen.getCantidadDisponible() < movimiento.getCantidad()) {
            throw new RuntimeException("No hay suficiente stock en bodega origen para transferir");
        }

        origen.setCantidadDisponible(origen.getCantidadDisponible() - movimiento.getCantidad());
        destino.setCantidadDisponible(destino.getCantidadDisponible() + movimiento.getCantidad());
        origen.setUltimaActualizacion(LocalDateTime.now());
        destino.setUltimaActualizacion(LocalDateTime.now());

        stockRepository.save(origen);
        stockRepository.save(destino);
    }
}
