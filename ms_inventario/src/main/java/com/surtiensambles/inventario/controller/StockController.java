package com.surtiensambles.inventario.controller;

import com.surtiensambles.inventario.dto.PageRequestDto;
import com.surtiensambles.inventario.entity.Stock;
import com.surtiensambles.inventario.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockService service;

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

        Page<Stock> pagina = service.listarPaginado(requestDto);
        return ResponseEntity.ok(pagina);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Stock> obtener(@PathVariable Long id) {
        Stock stock = service.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro de stock no encontrado"));
        return ResponseEntity.ok(stock);
    }
    
    // No hay métodos POST/PUT/DELETE aquí porque el stock se gestiona mediante Movimientos
}
