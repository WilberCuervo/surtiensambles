package com.surtiensambles.inventario.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReservaRequestDto {
    private Long productoId;
    private Long bodegaId;
    private Integer cantidad;
    private String nota;     // Nombre del cliente o detalles
    private String usuario;  // Quién atendió
    private LocalDateTime fechaExpiracion; // Opcional (puede ser null)
}