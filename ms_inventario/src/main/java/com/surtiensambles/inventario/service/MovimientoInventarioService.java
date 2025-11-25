package com.surtiensambles.inventario.service;

import com.surtiensambles.inventario.dto.MovimientoRequestDto;
import com.surtiensambles.inventario.dto.PageRequestDto;
import com.surtiensambles.inventario.entity.MovimientoInventario;
import org.springframework.data.domain.Page;

public interface MovimientoInventarioService {

    MovimientoInventario registrarMovimiento(MovimientoRequestDto requestDto);

    Page<MovimientoInventario> listarPaginado(PageRequestDto requestDto);
}