package com.surtiensambles.inventario.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reserva_inventario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservaInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "bodega_id", nullable = false)
    private Bodega bodega;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "referencia_reserva", unique = true, nullable = false, length = 100)
    private String referenciaReserva;

    @Column(name = "fecha_reserva", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime fechaReserva;

    @Column(name = "fecha_expiracion")
    private LocalDateTime fechaExpiracion;

    @Column(length = 20)
    private String estado; // ACTIVA, CONSUMIDA, EXPIRADA, CANCELADA
}
