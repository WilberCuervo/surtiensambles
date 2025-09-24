package com.jtccia.embargos.security.clasic.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@Data
@AllArgsConstructor
@Entity
@Table(name = "permiso")
public class PermisoClasic {

    @Id
    @Column(name = "id_permiso")
    private String id;

    private String descripcion;

    @Column(name = "nombre_opcion")
    private String nombreOpcion;
}

