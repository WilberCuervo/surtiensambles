package com.surtiensambes.commos.producto.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Clasificacion {
	private String grupo;
	private String subgrupo;
	private String linea;
	private List<String> etiquetas; 

}
