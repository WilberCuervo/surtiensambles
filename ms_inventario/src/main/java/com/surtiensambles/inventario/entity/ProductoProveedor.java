package com.surtiensambles.inventario.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "producto_proveedor")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ProductoProveedorId.class)
public class ProductoProveedor {

    @Id
    @Column(name = "producto_id")
    private Long productoId;

    @Id
    @Column(name = "proveedor_id")
    private Long proveedorId;
}
