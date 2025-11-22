package com.surtiensambles.inventario.controller;

import com.surtiensambles.inventario.entity.Bodega;
import com.surtiensambles.inventario.service.BodegaService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/bodegas")
public class BodegaController {

    private final BodegaService bodegaService;

    public BodegaController(BodegaService bodegaService) {
        this.bodegaService = bodegaService;
    }

    @GetMapping
    public List<Bodega> getAll() {
        return bodegaService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bodega> getById(@PathVariable Long id) {
        return bodegaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Bodega create(@RequestBody Bodega bodega) {
        return bodegaService.save(bodega);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Bodega> update(@PathVariable Long id, @RequestBody Bodega bodega) {
        return bodegaService.findById(id)
                .map(existing -> {
                    bodega.setId(id);
                    return ResponseEntity.ok(bodegaService.save(bodega));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bodegaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
