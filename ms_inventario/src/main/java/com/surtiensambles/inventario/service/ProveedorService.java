package com.surtiensambles.inventario.service;

import com.surtiensambles.inventario.dto.PageRequestDto;
import com.surtiensambles.inventario.entity.Proveedor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ProveedorService {

    // Método principal para listar paginado y filtrado usando el DTO
    Page<Proveedor> listarPaginado(PageRequestDto requestDto);

    // Método para obtener todos los proveedores sin paginar (para dropdowns/listas simples)
    List<Proveedor> listarPorEstado(Optional<Boolean> activo);
    
    // Métodos CRUD básicos
    Proveedor obtener(Long id);
    Proveedor crear(Proveedor proveedor);
    Proveedor actualizar(Long id, Proveedor proveedor);
    void eliminar(Long id);


    boolean existeNit(String nit);
}
