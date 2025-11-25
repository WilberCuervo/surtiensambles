package com.surtiensambles.inventario.dto;

import lombok.Data;

@Data
public class MovimientoRequestDto {
    private Long productoId;
    
    // Para Entradas/Salidas simples basta con bodegaId
    private Long bodegaId; 
    
    // Para Transferencias, el front enviará origen y destino
    private Long bodegaOrigenId;
    private Long bodegaDestinoId;

    private String tipo; // ENTRADA, SALIDA, AJUSTE, TRANSFERENCIA
    private Integer cantidad;
    
    private String referenciaTipo; // Opcional (ej: "VENTA", "COMPRA")
    private String referenciaId;   // Opcional (ej: "FAC-123")
    private String nota;
    private String usuario;
}