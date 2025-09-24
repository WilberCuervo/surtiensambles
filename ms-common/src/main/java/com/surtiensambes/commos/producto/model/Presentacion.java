package com.surtiensambes.commos.producto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Presentacion {
	private String idPresentacion;
	private String descripcion;
	private String unidadMedida;
	private Integer factorConversion;
	private imagen imagen;
	private Atrubutos atributos; 
	private Seo seo;
	
}
