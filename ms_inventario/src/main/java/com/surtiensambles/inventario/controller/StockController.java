package com.surtiensambles.inventario.controller;

import com.surtiensambles.inventario.entity.MovimientoInventario;
import com.surtiensambles.inventario.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    /**
     * Actualiza el stock de un producto en una bodega.
     * Se usa para movimientos de tipo ENTRADA, SALIDA o AJUSTE.
     */
    @PostMapping("/actualizar")
    public ResponseEntity<String> actualizarStock(@RequestBody MovimientoInventario movimiento) {
        stockService.actualizarStock(movimiento);
        return ResponseEntity.ok("Stock actualizado correctamente");
    }

    /**
     * Transfiere stock entre dos bodegas.
     * - movimiento.bodegaId = bodega origen
     * - movimiento.referenciaId = id de la bodega destino
     * - movimiento.cantidad = cantidad a transferir
     */
    @PostMapping("/transferir")
    public ResponseEntity<String> transferirStock(@RequestBody MovimientoInventario movimiento) {
        stockService.transferir(movimiento);
        return ResponseEntity.ok("Transferencia de stock realizada correctamente");
    }
}
