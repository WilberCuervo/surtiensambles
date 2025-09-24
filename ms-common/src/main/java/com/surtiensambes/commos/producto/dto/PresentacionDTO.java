package com.surtiensambes.commos.producto.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PresentacionDTO {

    @NotBlank(message = "El ID de presentación es obligatorio")
    private String idPresentacion;

    @NotBlank(message = "La descripción de la presentación es obligatoria")
    @Size(min = 3, max = 200, message = "La descripción debe tener entre 3 y 200 caracteres")
    private String descripcion;

    @NotBlank(message = "La unidad de medida es obligatoria")
    private String unidadMedida;

    @Positive(message = "El factor de conversión debe ser mayor a 0")
    private double factorConversion;

    @Pattern(
        regexp = "^(http|https)://.*$",
        message = "La URL de la imagen debe ser válida"
    )
    private String imagen;
}