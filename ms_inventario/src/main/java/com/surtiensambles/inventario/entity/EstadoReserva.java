package com.surtiensambles.inventario.entity;

public enum EstadoReserva {
    ACTIVA,      // La mercancía está apartada
    CONSUMIDA,   // El cliente vino, pagó y se convirtió en Venta (Salida)
    EXPIRADA,    // El cliente no vino a tiempo, el sistema libera el stock
    CANCELADA    // Se canceló manualmente
}