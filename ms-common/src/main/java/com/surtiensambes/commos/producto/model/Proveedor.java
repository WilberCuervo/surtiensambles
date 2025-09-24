package com.surtiensambes.commos.producto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Proveedor {
	private String codigoFabricante;
	private String paisOrigen;
	private String nombre;
	private String contacto;
	private String telefono;
	private String celuar;
	private String correo;

}
