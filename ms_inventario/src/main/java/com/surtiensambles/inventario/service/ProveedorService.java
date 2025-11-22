package com.surtiensambles.inventario.service;

import com.surtiensambles.inventario.entity.Proveedor;
import org.springframework.data.domain.Page;

public interface ProveedorService {
    
    Page<Proveedor> list(int page, int size, String search, Boolean activo, String sort);
    
    Proveedor get(Long id);
    Proveedor create(Proveedor proveedor);
    Proveedor update(Long id, Proveedor proveedor);
    void delete(Long id);
    boolean existeNit(String nit);
}
