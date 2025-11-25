package com.surtiensambles.inventario.service;

import com.surtiensambles.inventario.dto.PageRequestDto;
import com.surtiensambles.inventario.entity.Stock;
import org.springframework.data.domain.Page;
import java.util.Optional;

public interface StockService {
	
    // Método principal para listar paginado y filtrado usando el DTO
    Page<Stock> listarPaginado(PageRequestDto requestDto);
    
    // Método para obtener un registro de stock específico (por si acaso)
    Optional<Stock> findById(Long id);
    
    // Método para para actualizar el inventario.
    //Maneja la lógica de suma/resta y validación de negativos.
    void actualizarStock(Long productoId, Long bodegaId, String tipoMovimiento, Integer cantidad);
}
