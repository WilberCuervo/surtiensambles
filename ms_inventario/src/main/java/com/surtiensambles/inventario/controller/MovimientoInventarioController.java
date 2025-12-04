package com.surtiensambles.inventario.controller;

import com.surtiensambles.inventario.dto.MovimientoRequestDto;
import com.surtiensambles.inventario.dto.PageRequestDto;
import com.surtiensambles.inventario.entity.MovimientoInventario;
import com.surtiensambles.inventario.service.MovimientoInventarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/movimientos")
@RequiredArgsConstructor
public class MovimientoInventarioController {

    private final MovimientoInventarioService movimientoService;

    @GetMapping
    public ResponseEntity<Page<MovimientoInventario>> getAllPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "") String search,
            
            // --- NUEVOS PARÁMETROS DE FECHA ---
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            // ----------------------------------

            @RequestParam(defaultValue = "fecha") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection
    ) {
        PageRequestDto requestDto = new PageRequestDto();
        requestDto.setPage(page);
        requestDto.setSize(size);
        requestDto.setSearch(search);
        
        // Seteamos las fechas en el DTO
        requestDto.setFechaInicio(fechaInicio);
        requestDto.setFechaFin(fechaFin);

        requestDto.setSortBy(sortBy);
        requestDto.setSortDirection(sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC);

        return ResponseEntity.ok(movimientoService.listarPaginado(requestDto));
    }

    @PostMapping
    public ResponseEntity<MovimientoInventario> create(@RequestBody MovimientoRequestDto dto) {
        MovimientoInventario nuevo = movimientoService.registrarMovimiento(dto);
        return ResponseEntity.status(201).body(nuevo);
    }
}