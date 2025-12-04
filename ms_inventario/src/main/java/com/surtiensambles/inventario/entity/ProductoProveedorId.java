package com.surtiensambles.inventario.entity;

import lombok.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoProveedorId implements Serializable {
    private Long productoId;
    private Long proveedorId;
}
	