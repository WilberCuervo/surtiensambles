package com.surtiensambles.inventario.controller;

import com.surtiensambles.inventario.dto.PageRequestDto;
import com.surtiensambles.inventario.entity.Stock;
import com.surtiensambles.inventario.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    /**
     * Endpoint para obtener productos con stock bajo (críticos).
     * Ruta: /api/stock/alertas
     */
    @GetMapping("/alertas")
    public ResponseEntity<List<Stock>> getAlertasReabastecimiento(
            @RequestParam(required = false) Long bodegaId
    ) {
        return ResponseEntity.ok(stockService.obtenerReporteReabastecimiento(bodegaId));
    }

    /**
     * Endpoint principal para listado paginado y filtrado de stock.
     */
    @GetMapping
    public ResponseEntity<Page<Stock>> listarPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection
    ) {
        PageRequestDto requestDto = new PageRequestDto();
        requestDto.setPage(page);
        requestDto.setSize(size);
        requestDto.setSearch(search);
        requestDto.setSortBy(sortBy);
        requestDto.setSortDirection(sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC);

        return ResponseEntity.ok(stockService.listarPaginado(requestDto));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Stock> obtener(@PathVariable Long id) {
        return stockService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("Registro de stock no encontrado"));
    }
}