package com.surtiensambles.inventario.controller;

import com.surtiensambles.inventario.dto.PageRequestDto;
import com.surtiensambles.inventario.entity.Proveedor;
import com.surtiensambles.inventario.service.ProveedorService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/proveedores")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class ProveedorController {

    private final ProveedorService service;

    /**
     * Endpoint principal para listado paginado y filtrado de proveedores.
     * Utiliza PageRequestDto para una interfaz de servicio limpia.
     */
    @GetMapping
    public ResponseEntity<Page<Proveedor>> listarPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam(required = false) Boolean activo,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection
    ) {
        // Mapea los RequestParams al DTO y delega al servicio
        PageRequestDto requestDto = new PageRequestDto();
        requestDto.setPage(page);
        requestDto.setSize(size);
        requestDto.setSearch(search);
        requestDto.setActivo(activo);
        requestDto.setSortBy(sortBy);
        requestDto.setSortDirection(sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC);

        Page<Proveedor> pagina = service.listarPaginado(requestDto);
        return ResponseEntity.ok(pagina);
    }
    
    /**
     * Endpoint para obtener TODOS los proveedores (para dropdowns/selects u otras listas).
     * Acepta un parámetro 'activo' opcional para filtrar.
     */
    @GetMapping("/all")
    public ResponseEntity<List<Proveedor>> listarTodosOporEstado(@RequestParam Optional<Boolean> activo) {
        List<Proveedor> proveedores = service.listarPorEstado(activo);
        return ResponseEntity.ok(proveedores);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Proveedor> obtener(@PathVariable Long id) {
        Proveedor proveedor = service.obtener(id);
        return ResponseEntity.ok(proveedor);
    }

    @PostMapping
    public ResponseEntity<Proveedor> crear(@RequestBody Proveedor proveedor) {
        Proveedor nuevoProveedor = service.crear(proveedor);
        return ResponseEntity.status(201).body(nuevoProveedor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Proveedor> actualizar(@PathVariable Long id, @RequestBody Proveedor proveedor) {
        Proveedor proveedorActualizado = service.actualizar(id, proveedor);
        return ResponseEntity.ok(proveedorActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/existe")
    public ResponseEntity<Boolean> existeNit(@RequestParam String nit) {
        boolean existe = service.existeNit(nit);
        return ResponseEntity.ok(existe);
    }
}
