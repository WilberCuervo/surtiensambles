package com.surtiensambles.inventario.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bodega")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bodega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codigo;

    @Column(nullable = false)
    private String nombre;

    private String ubicacion;
    private String responsable;

    @Column(nullable = false)
    private Boolean estado = true;
    
}
