package com.surtiensambes.commos.producto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Otros {
	private String codigoFabricante;
	private String paisOrigen;
	private Integer garantiaMeses;
	private String temperaturaAlmacenamiento;
	private String humedadRecomendada;
	private String requiereManejoEspecial;
	private Proveedor proveedor;
	private String stockMinimo;
	private String stockMaximo;
	private String vidaUtil;
	private EstadoInventario estadoInventario;
	

}
