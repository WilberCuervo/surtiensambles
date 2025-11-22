package com.surtiensambles.inventario.controller;

import com.surtiensambles.inventario.entity.Proveedor;
import com.surtiensambles.inventario.service.ProveedorService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/proveedores")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class ProveedorController {

    private final ProveedorService service;

    @GetMapping
    public ResponseEntity<Page<Proveedor>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(required = false) Boolean activo,
            @RequestParam(defaultValue = "id,asc") String sort) {
        
        Page<Proveedor> proveedores = service.list(page, size, search, activo, sort);
        return ResponseEntity.ok(proveedores);
    }

    @GetMapping("/{id}")
    public Proveedor get(@PathVariable Long id) {
        return service.get(id);
    }

    @PostMapping
    public Proveedor create(@RequestBody Proveedor proveedor) {
        return service.create(proveedor);
    }

    @PutMapping("/{id}")
    public Proveedor update(@PathVariable Long id, @RequestBody Proveedor proveedor) {
        return service.update(id, proveedor);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/existe")
    public boolean existeNit(@RequestParam String nit) {
        return service.existeNit(nit);
    }
}
