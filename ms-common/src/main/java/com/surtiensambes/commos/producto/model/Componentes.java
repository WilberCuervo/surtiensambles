package com.surtiensambes.commos.producto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Componentes {
	private Long idProduct;
	private String codigo;
	private String cantidad;
	private String observaciones;	

}
