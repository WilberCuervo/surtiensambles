package com.surtiensambes.commos.producto.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Clasificacion {
	
	 private String grupo;
     private String subgrupo;
     private String linea;
     private List<String> etiquetas;

}
