package com.surtiensambles.inventario.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "reserva_inventario")
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    // Código único para reclamar la reserva (Ej: "RES-9090")
    @Column(name = "referencia_reserva", unique = true, nullable = false)
    private String referenciaReserva;

    @Column(name = "fecha_reserva")
    private LocalDateTime fechaReserva = LocalDateTime.now();

    //Cuándo se vence la reserva. Si pasa esta fecha, el sistema debería liberar el stock.
    @Column(name = "fecha_expiracion")
    private LocalDateTime fechaExpiracion;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EstadoReserva estado; // ACTIVA, CONSUMIDA, EXPIRADA, CANCELADA

    private String usuario;
    private String nota;
}