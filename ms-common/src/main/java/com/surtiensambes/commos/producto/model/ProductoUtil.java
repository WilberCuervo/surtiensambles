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
public class ProductoUtil {
	
	
	    private Long id;
	    private boolean esPromocion;
	    private boolean esDestacado;
	    private boolean esCompuesto;
	    private boolean esNuevo;
	    private boolean esOferta;
	    private String visibilidad;	    
	    private Clasificacion clasificacion;
	    private ProductoFacturacion productoFacturacion;
	    private Componentes componentes;
	    private Otros otros;
	    private DatosSistema datosSistema;
	    private List<Presentacion> presentaciones;
	    private List<PreciosSucursal> preciosSucursal;

}
