package com.surtiensambes.commos.producto.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ProductoFacturacion {
	
	private Relevantes relevantes;
    private List<Impuestos> impuestos;
    private EstandarIdentificacion estandarIdentificacion;
    private MandanteOperacionVenta mandanteOperacionVeta;


}
