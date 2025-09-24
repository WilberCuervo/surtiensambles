package com.surtiensambes.commos.producto.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductoDTO {

    /*@NotBlank(message = "El ID del producto es obligatorio")
    private String idProducto;*/

    
	@NotBlank(message = "El codigo del producto es obligatorio")
    @Size(min = 3, max = 100, message = "El codigo debe tener entre 3 y 100 caracteres")
    private String codigo;
	
	@NotBlank(message = "El nombre del producto es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombre;
	
	@Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
    private String descripcion;
	
	@Size(min = 3, max = 150, message = "La marca debe tener entre 3 y 150 caracteres")
	private String marca;
	
	@Size(min = 3, max = 150, message = "La marca debe tener entre 3 y 150 caracteres")
	private String modelo;

    private Clasificacion clasificacion;
    
    @Size(max = 10, message = " el tipo de producto no supera los 10 caracteres")
    private String tipoProducto;//fisico digital o servicio

    @Valid
    @NotEmpty(message = "Debe tener al menos una presentación")
    private List<PresentacionDTO> presentaciones;

    @Valid
    @NotEmpty(message = "Debe tener precios configurados por sucursal")
    private List<PrecioSucursalDTO> preciosSucursal;
}