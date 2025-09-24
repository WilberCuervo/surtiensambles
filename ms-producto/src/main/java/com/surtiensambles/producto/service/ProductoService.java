package com.surtiensambles.producto.service;

import java.util.Optional;

import com.surtiensambes.commos.producto.dto.ProductoDTO;
import com.surtiensambles.producto.entity.Producto;

public interface ProductoService {
    public ProductoDTO  createProducto(ProductoDTO  producto);     
	public Optional<Producto > getProducto(Long id);
	public void juan();
}
