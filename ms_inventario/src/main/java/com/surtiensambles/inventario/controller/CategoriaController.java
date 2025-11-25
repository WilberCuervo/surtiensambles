package com.surtiensambles.inventario.controller;

import com.surtiensambles.inventario.dto.PageRequestDto;
import com.surtiensambles.inventario.entity.Categoria;
import com.surtiensambles.inventario.service.CategoriaService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService service;

    /**
     * Endpoint principal para listado paginado y filtrado.
     * Recibe todos los parámetros como RequestParams y los mapea al DTO para el servicio.
     * Responde con un objeto Page<Categoria> de Spring estándar.
     */
    @GetMapping
    public ResponseEntity<Page<Categoria>> listarPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(required = false) Boolean activo,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection
    ) {
        // Creamos el DTO con los parámetros recibidos
        PageRequestDto requestDto = new PageRequestDto();
        requestDto.setPage(page);
        requestDto.setSize(size);
        requestDto.setSearch(search);
        requestDto.setActivo(activo);
        requestDto.setSortBy(sortBy);
        // Manejamos la dirección del sort de String a Enum de Spring
        requestDto.setSortDirection(sortDirection.equalsIgnoreCase("desc") ? org.springframework.data.domain.Sort.Direction.DESC : org.springframework.data.domain.Sort.Direction.ASC);

        Page<Categoria> pagina = service.listarPaginado(requestDto);
        return ResponseEntity.ok(pagina);
    }

    /**
     * Endpoint para obtener TODAS las categorías (para dropdowns/selects).
     * Acepta un parámetro 'activo' opcional para filtrar.
     * Ejemplo 1: GET /api/categorias/all -> Devuelve todas (activas e inactivos).
     * Ejemplo 2: GET /api/categorias/all?activo=true -> Devuelve solo activas.
     * Ejemplo 3: GET /api/categorias/all?activo=false -> Devuelve solo inactivos.
     */
    @GetMapping("/all")
    public ResponseEntity<List<Categoria>> listarTodasOporEstado(@RequestParam Optional<Boolean> activo) {
        List<Categoria> categorias = service.listarPorEstado(activo);
        return ResponseEntity.ok(categorias);
    }
    

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> obtener(@PathVariable Long id) {
        Categoria categoria = service.obtener(id);
        return ResponseEntity.ok(categoria);
    }

    @PostMapping
    public ResponseEntity<Categoria> crear(@RequestBody Categoria categoria) {
        Categoria nuevaCategoria = service.crear(categoria);
        return ResponseEntity.status(201).body(nuevaCategoria);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categoria> actualizar(@PathVariable Long id, @RequestBody Categoria categoria) {
        Categoria categoriaActualizada = service.actualizar(id, categoria);
        return ResponseEntity.ok(categoriaActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/existe")
    public ResponseEntity<Boolean> existe(@RequestParam String nombre) {
        boolean existe = service.existeNombre(nombre);
        return ResponseEntity.ok(existe);
    }
}
