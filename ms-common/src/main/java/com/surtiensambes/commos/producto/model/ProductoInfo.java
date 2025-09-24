package com.surtiensambes.commos.producto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoInfo {
	    private Long id;
	    private String codigo;
	    private String nombre;
	    private String descripcion;
	    private String marca;
	    private String modelo;
	    private Long idSucursalCliente;
	    private String datosTecnicos;	    
	    private String origen;
	    private String destino;
	    private String estado;	    

	}


