package com.surtiensambles.inventario.controller;

import com.surtiensambles.inventario.entity.ReservaInventario;
import com.surtiensambles.inventario.service.ReservaInventarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
public class ReservaInventarioController {

    private final ReservaInventarioService reservaService;

    @PostMapping("/crear")
    public ResponseEntity<ReservaInventario> crearReserva(@RequestBody ReservaInventario reserva) {
        return ResponseEntity.ok(reservaService.crearReserva(reserva));
    }

    @PostMapping("/consumir/{referencia}")
    public ResponseEntity<ReservaInventario> consumirReserva(@PathVariable String referencia) {
        return ResponseEntity.ok(reservaService.consumirReserva(referencia));
    }

    @PostMapping("/cancelar/{referencia}")
    public ResponseEntity<ReservaInventario> cancelarReserva(@PathVariable String referencia) {
        return ResponseEntity.ok(reservaService.cancelarReserva(referencia));
    }

    @GetMapping
    public ResponseEntity<List<ReservaInventario>> listarReservas() {
        return ResponseEntity.ok(reservaService.listarReservas());
    }
}
