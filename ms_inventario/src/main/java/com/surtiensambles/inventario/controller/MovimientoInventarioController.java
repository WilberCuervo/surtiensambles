package com.surtiensambles.inventario.controller;

import com.surtiensambles.inventario.entity.MovimientoInventario;
import com.surtiensambles.inventario.service.MovimientoInventarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/movimientos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // para permitir pruebas desde Angular o Postman
public class MovimientoInventarioController {

    private final MovimientoInventarioService movimientoInventarioService;

    /**
     * Registrar un nuevo movimiento de inventario.
     * Soporta ENTRADA, SALIDA, AJUSTE y TRANSFERENCIA.
     */
    @PostMapping
    public ResponseEntity<?> registrarMovimiento(@RequestBody MovimientoInventario movimiento) {
        try {
            // Validaciones básicas antes de llamar al service
            if (movimiento.getProducto() == null || movimiento.getProducto().getId() == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Debe indicar el producto (campo producto.id)."));
            }
            if (movimiento.getBodega() == null || movimiento.getBodega().getId() == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Debe indicar la bodega origen (campo bodega.id)."));
            }
            if (movimiento.getCantidad() == null || movimiento.getCantidad() <= 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "La cantidad debe ser mayor que cero."));
            }
            if (movimiento.getTipo() == null || movimiento.getTipo().isBlank()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Debe indicar el tipo de movimiento (ENTRADA, SALIDA, AJUSTE, TRANSFERENCIA)."));
            }

            // Validación específica para TRANSFERENCIA
            if ("TRANSFERENCIA".equalsIgnoreCase(movimiento.getTipo())) {
                if (movimiento.getReferenciaId() == null || movimiento.getReferenciaId().isBlank()) {
                    return ResponseEntity.badRequest()
                            .body(Map.of("error", "En una TRANSFERENCIA debe indicar la bodega destino en referenciaId."));
                }
                if (!movimiento.getReferenciaId().trim().matches("\\d+")) {
                    return ResponseEntity.badRequest()
                            .body(Map.of("error", "El campo referenciaId debe ser el ID numérico de la bodega destino."));
                }
            }

            MovimientoInventario guardado = movimientoInventarioService.registrarMovimiento(movimiento);
            return ResponseEntity.status(HttpStatus.CREATED).body(guardado);

        } catch (RuntimeException e) {
            // Errores de negocio controlados (stock insuficiente, no encontrada, etc.)
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            // Cualquier otro error inesperado
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor", "detalle", e.getMessage()));
        }
    }

    /**
     * Listar todos los movimientos de inventario.
     */
    @GetMapping
    public ResponseEntity<List<MovimientoInventario>> listarMovimientos() {
        List<MovimientoInventario> lista = movimientoInventarioService.listarMovimientos();
        return ResponseEntity.ok(lista);
    }
}
