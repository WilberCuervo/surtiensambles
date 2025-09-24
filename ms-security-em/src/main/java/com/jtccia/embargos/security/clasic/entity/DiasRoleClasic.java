package com.jtccia.embargos.security.clasic.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "dias_rol")
public class DiasRoleClasic {

    @Id
    @Column(name = "id_horario")
    private Integer id;

    private Boolean lunes;
    private Boolean martes;
    private Boolean miercoles;
    private Boolean jueves;
    private Boolean viernes;
    private Boolean sabado;
    private Boolean domingo;
    private Boolean festivos;

}

