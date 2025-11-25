package com.surtiensambles.inventario.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.surtiensambles.inventario.dto.PageRequestDto;
import com.surtiensambles.inventario.entity.Bodega;

public interface BodegaService {

    Page<Bodega> listarPaginado(PageRequestDto requestDto);

    List<Bodega> listarPorEstado(Optional<Boolean> activo);
    
    Bodega obtener(Long id);
    Bodega crear(Bodega bodega);
    Bodega actualizar(Long id, Bodega bodega);
    void eliminar(Long id);

    
    boolean existeCodigo(String codigo);
}
