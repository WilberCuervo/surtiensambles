package com.docelectronico.cliente.entity;


import java.time.LocalDateTime;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.surtiensambes.commos.cliente.model.Contacto;
import com.surtiensambes.commos.cliente.model.LocalizacionFisica;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name="clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "indice", unique = true, updatable = false)
	private Long indice;
	@Column(name="tipo_identificacion", nullable = false)	
	private Integer tipoId;
	@Column(name="nro_identificacion", nullable = false)
	private String nroId;
	@Column(name="digito_verif", nullable = false)
	private Integer digitoVerificacion;
	@Column(name="tipo_persona", nullable = false)
	private Integer tipoPersona;
	@Column(name="razon_social", nullable = true)
	private String razonSocial;
	@Column(name="primer_apellido", nullable = true)
	private String primerApellido;
	@Column(name="segundo_apellido", nullable = true)
	private String segundoApellido;
	@Column(name="primer_nombre", nullable = true)
	private String primerNombre;
	@Column(name="segundo_nombre", nullable = true)
	private String segundoNombre;	
	@Column(name="nombre_comercial", nullable = true)
	private String nombreComercial;
	@Column(name="correo_electronico", nullable = true)
	private String correo;	
	@Column(name="detalle_tributario", nullable = false)
	private String detalleTributario; 
	@Column(name="responsabilidad_fiscal", nullable = false)
	private String responsabilidadFiscal;
    @JdbcTypeCode(SqlTypes.JSON)
	@Column(name="localizacion_fisica", nullable = true, columnDefinition = "jsonb")
	private LocalizacionFisica localizacionFisica;
    @JsonInclude(Include.NON_NULL)
    @JdbcTypeCode(SqlTypes.JSON)
	@Column(name="contacto", nullable = true, columnDefinition = "jsonb")
	private Contacto contacto;
	@Column(name="fecha_creacion", nullable = true)
	private LocalDateTime fechaCreacion;
	@Column(name="usuario_creacion", nullable = true)
	private String usuarioCreacion;
	@Column(name="fecha_modificacion", nullable = true)
	private LocalDateTime fechaModificacion;
	@Column(name="usuario_modificacion", nullable = true)
	private String usuarioModificacion;	
	@Column(name="certificado_firma", nullable = true)
	private String certificadoFirma;
	@Column(name="testsetid", nullable = true)
	private String testSetId;
	@Column(name="tipo_ambiente", nullable = false)
	private String tipoAmbiente;	
	@Column(name="estado", nullable = false)
	private String estado;
	
   
}
