package com.jtccia.embargos.security.clasic.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "banco")
public class BancoClasic {
    @Id
    @Column(name = "id_banco")
    private Integer id;

    private String nombre;

    private Long nit;

    @Column(name = "digito_verificacion")
    private Integer digitoVerificacion;

    @Column(name = "nombre_funcionario")
    private String nombreFuncionario;

    @Column(name = "num_id_funcionario")
    private Long numIdFuncionario;

    private Boolean estado;

    @Column(name = "tiempo_sesion")
    private Integer tiempoSesion;
}

