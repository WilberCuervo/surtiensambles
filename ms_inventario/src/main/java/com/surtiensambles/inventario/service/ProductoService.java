package com.surtiensambles.inventario.service;

import com.surtiensambles.inventario.dto.PageRequestDto;
import com.surtiensambles.inventario.entity.Producto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ProductoService {

    // Método principal para listar paginado y filtrado (usando DTO)
    Page<Producto> listarPaginado(PageRequestDto requestDto);

    // Método para obtener todos los productos sin paginar (para dropdowns)
    List<Producto> listarPorEstado(Optional<Boolean> activo);
    
    // Métodos CRUD básicos
    Producto crear(Producto producto); 

    List<Producto> findAll(); 

    Optional<Producto> findById(Long id);
    
    Producto actualizar(Long id, Producto producto); 

    void delete(Long id);

    
    boolean existsBySku(String sku);

    
}
