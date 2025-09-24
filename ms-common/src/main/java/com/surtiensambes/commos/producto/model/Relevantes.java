package com.surtiensambes.commos.producto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Relevantes {
	private String referencia;
    private String posicionArancelaria;
    private String descripcionAdicional;
    private InformacionAdicional informacionAdicional;
    private Double precioUnitario;
    private Double volumenTotal;
    private Double volumenValidado;
    private Integer gramosTotal;
    private Double volumen;
    private String tipo; 

}
