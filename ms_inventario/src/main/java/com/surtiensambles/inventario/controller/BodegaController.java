package com.surtiensambles.inventario.controller;

import com.surtiensambles.inventario.dto.PageRequestDto;
import com.surtiensambles.inventario.entity.Bodega;
import com.surtiensambles.inventario.service.BodegaService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/bodegas")
@RequiredArgsConstructor
public class BodegaController {

    private final BodegaService service;

    /**
     * Endpoint principal para listado paginado y filtrado de bodegas.
     */
    @GetMapping
    public ResponseEntity<Page<Bodega>> listarPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam(required = false) Boolean activo,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection
    ) {
        PageRequestDto requestDto = new PageRequestDto();
        requestDto.setPage(page);
        requestDto.setSize(size);
        requestDto.setSearch(search);
        requestDto.setActivo(activo);
        requestDto.setSortBy(sortBy);
        requestDto.setSortDirection(sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC);

        Page<Bodega> pagina = service.listarPaginado(requestDto);
        return ResponseEntity.ok(pagina);
    }
    
    /**
     * Endpoint para obtener TODAS las bodegas (para dropdowns/selects u otras listas).
     */
    @GetMapping("/all")
    public ResponseEntity<List<Bodega>> listarTodasOporEstado(@RequestParam Optional<Boolean> activo) {
        List<Bodega> bodegas = service.listarPorEstado(activo);
        return ResponseEntity.ok(bodegas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bodega> obtener(@PathVariable Long id) {
        Bodega bodega = service.obtener(id);
        return ResponseEntity.ok(bodega);
    }

    @PostMapping
    public ResponseEntity<Bodega> crear(@RequestBody Bodega bodega) {
        Bodega nuevaBodega = service.crear(bodega);
        return ResponseEntity.status(201).body(nuevaBodega);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Bodega> actualizar(@PathVariable Long id, @RequestBody Bodega bodega) {
        Bodega bodegaActualizada = service.actualizar(id, bodega);
        return ResponseEntity.ok(bodegaActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/existeCodigo")
    public ResponseEntity<Boolean> existeCodigo(@RequestParam String codigo) {
        boolean existe = service.existeCodigo(codigo);
        return ResponseEntity.ok(existe);
    }
}
