package com.surtiensambes.commos.producto.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DatosSistema {
	 private LocalDateTime  fechaInicioVigencia;
	 private LocalDateTime  fechaFinVigencia;
	 private String estadoProducto ;
	 private LocalDateTime fechaCreacion ;
	 private String usuarioCreacion;
	 private LocalDateTime fechaModificacion;
	 private String usuarioModificacion;

}
