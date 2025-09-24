package com.jtccia.embargos.security.clasic.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "estado")
public class EstadoClasic {
    @Id
    @Column(name = "id_estado")
    private Integer id;

    private String nombre;
}
