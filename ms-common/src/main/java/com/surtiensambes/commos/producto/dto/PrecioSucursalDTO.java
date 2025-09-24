package com.surtiensambes.commos.producto.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PrecioSucursalDTO {

    @NotBlank(message = "El nombre de la sucursal es obligatorio")
    private String nombreSucursal;

    @NotNull(message = "El ID de la sucursal es obligatorio")
    private Long idSucursal;

    @Valid
    @NotEmpty(message = "Debe tener presentaciones configuradas para la sucursal")
    private List<PresentacionSucursalDTO> presentacionSucursal;
}
