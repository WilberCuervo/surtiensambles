package com.surtiensambles.inventario.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "producto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sku", nullable = false, unique = true)
    private String sku;

    @Column(nullable = false)
    private String nombre;

    private String descripcion;

    @Column(name = "unidad_medida")
    private String unidad_medida;

    @Column(name = "precio_referencia")
    private Double precio_referencia;

    @Column(name = "nivel_reorden")
    private Integer nivelReorden;

    @Column(name = "activo")
    private Boolean activo = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
    

}
