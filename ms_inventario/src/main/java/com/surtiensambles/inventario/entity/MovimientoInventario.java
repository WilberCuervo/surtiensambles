package com.surtiensambles.inventario.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimiento_inventario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovimientoInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "bodega_id", nullable = false)
    private Bodega bodega;

    @Column(nullable = false, length = 20)
    private String tipo; // ENTRADA, SALIDA, AJUSTE, TRANSFERENCIA

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "referencia_tipo", length = 50)
    private String referenciaTipo; // Ej: FACTURA, PEDIDO, AJUSTE

    @Column(name = "referencia_id", length = 100)
    private String referenciaId;

    @Column(length = 100)
    private String usuario;

    @Column(length = 255)
    private String nota;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime fecha;
}
