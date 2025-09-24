package com.docelectronico.usuario.entity;

import com.docelectronico.usuario.model.Perfil;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "usuarios")
public class Usuario {
		
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long indice;

	    @Column(name = "nombre_usuario", nullable = false, unique = true)
	    private String nombreUsuario;

	    @Column(name = "contrasena", nullable = false)
	    private String contrasena;

	    @ManyToOne
	    @JoinColumn(name = "id_perfil", nullable = false)
	    private Perfil perfil;

	    @Column(name = "nombre", nullable = false)
	    private String nombre;

	    @Column(name = "correo_electronico", nullable = false)
	    private String correoElectronico;

	    @Column(name = "estado", nullable = false)
	    private String estado;

	    
	}

