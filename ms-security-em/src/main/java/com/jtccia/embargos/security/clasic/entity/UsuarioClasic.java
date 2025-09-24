package com.jtccia.embargos.security.clasic.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "usuario")
public class UsuarioClasic {
	
	@Id
    private String login;

    @Column(nullable = false)
    private String clave;

    @Column(name = "primer_nombre", nullable = false)
    private String primerNombre;

    @Column(name = "segundo_nombre")
    private String segundoNombre;

    @Column(name = "primer_apellido", nullable = false)
    private String primerApellido;

    @Column(name = "segundo_apellido")
    private String segundoApellido;

    private String correo;

    @ManyToOne
    @JoinColumn(name = "id_estado", nullable = false)
    private EstadoClasic estado;

    @Column(name = "fecha_registro")
    private LocalDate fechaRegistro;

    @Column(name = "correo_alterno")
    private String correoAlterno;

    private Integer intento;

    @Column(name = "fecha_intento")
    private LocalDate fechaIntento;

    @Column(name = "firma_digital")
    private String firmaDigital;

    private String cargo;

    @Column(name = "fecha_cambio_contrasena")
    private LocalDateTime fechaCambioContrasena;

    private Boolean externo;

    @Column(name = "token_recuperacion")
    private String tokenRecuperacion;

    @Column(name = "token_expiracion")
    private LocalDateTime tokenExpiracion;

    @Column(name = "token_utilizado")
    private Boolean tokenUtilizado;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "rol_usuario",
        joinColumns = @JoinColumn(name = "login"),
        inverseJoinColumns = @JoinColumn(name = "id_rol")
    )
    private Set<RolClasic> roles = new HashSet<>();
	}


