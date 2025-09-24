package com.surtiensambes.commos.producto.model;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PresentacionSucursal {
	private String idPresentacion;
	private String tipoVenta;
	@NotNull(message = "El precio es obligatorio")
	@DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
	private BigDecimal  precio;
	private BigDecimal  precioCompra;
	private BigDecimal  margenGanancia;
	private String ubicacion;
	private String codigoBarras;
	private String codigoQR;

}
