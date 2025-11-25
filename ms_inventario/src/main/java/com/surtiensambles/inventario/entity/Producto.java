package com.surtiensambles.inventario.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "producto")
@Data
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String sku;

    @Column(nullable = false)
    private String nombre;

    private String descripcion;

    @Column(name = "unidad_medida")
    private String unidadMedida;

    @Column(name = "precio_referencia")
    private BigDecimal precioReferencia; 

    @Column(name = "nivel_reorden")
    private Integer nivelReorden;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    private Boolean activo;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}