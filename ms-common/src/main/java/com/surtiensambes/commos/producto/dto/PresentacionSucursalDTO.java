package com.surtiensambes.commos.producto.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PresentacionSucursalDTO {

    @NotBlank(message = "El ID de presentación es obligatorio")
    private String idPresentacion;

    @NotBlank(message = "El tipo de venta es obligatorio")
    private String tipoVenta;

    @Positive(message = "El precio debe ser mayor que 0")
    private double precio;

    @PositiveOrZero(message = "El precio de compra no puede ser negativo")
    private double precioCompra;

    @PositiveOrZero(message = "El margen de ganancia no puede ser negativo")
    private double margenGanancia;

    @NotBlank(message = "La ubicación es obligatoria")
    @Size(max = 200, message = "La ubicación no puede superar los 200 caracteres")
    private String ubicacion;

    @Pattern(regexp = "^[0-9]{13}$", message = "El código de barras debe tener 13 dígitos")
    private String codigoBarras;

    @Pattern(
        regexp = "^(http|https)://.*$",
        message = "El código QR debe ser una URL válida"
    )
    private String codigoQR;
}
