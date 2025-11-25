package com.surtiensambles.inventario.controller;

import com.surtiensambles.inventario.dto.ReservaRequestDto;
import com.surtiensambles.inventario.entity.ReservaInventario;
import com.surtiensambles.inventario.serviceImpl.ReservaInventarioServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ReservaInventarioController {

    private final ReservaInventarioServiceImpl reservaService;

    // Crear Reserva
    @PostMapping
    public ResponseEntity<ReservaInventario> crear(@RequestBody ReservaRequestDto dto) {
        return ResponseEntity.status(201).body(reservaService.crearReserva(dto));
    }

    // Cancelar (Devolver al stock)
    @PostMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        reservaService.cancelarReserva(id);
        return ResponseEntity.ok().build();
    }

    // Confirmar Venta (Sacar del inventario)
    @PostMapping("/{id}/confirmar")
    public ResponseEntity<Void> confirmar(@PathVariable Long id) {
        reservaService.confirmarVentaReserva(id);
        return ResponseEntity.ok().build();
    }
    
    // Listar solo las reservas pendientes (ACTIVAS)
    @GetMapping("/activas")
    public ResponseEntity<List<ReservaInventario>> getReservasActivas(
    		
            @RequestParam(required = false) Long bodegaId // Parámetro opcional
            
    ) {
        return ResponseEntity.ok(reservaService.listarReservasActivas(bodegaId));
    }
}