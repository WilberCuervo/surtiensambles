package com.surtiensambles.inventario.service;

import com.surtiensambles.inventario.entity.MovimientoInventario;

public interface StockService {
    void actualizarStock(MovimientoInventario movimiento);
    /**
     * Transfiere cantidad del producto entre dos bodegas (origen -> destino).
     * - movimiento.bodega = bodega origen
     * - movimiento.referenciaId = id (String) de la bodega destino (convertible a Long)
     * - movimiento.cantidad = cantidad a transferir
     */
    void transferir(MovimientoInventario movimiento);
}
