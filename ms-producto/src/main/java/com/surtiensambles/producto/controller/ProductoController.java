package com.surtiensambles.producto.controller;

import com.surtiensambes.commos.producto.dto.ProductoDTO;
import com.surtiensambes.commos.utils.ResponseController;

import com.surtiensambles.producto.entity.Producto;
import com.surtiensambles.producto.service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @PostMapping
    public ResponseEntity<ResponseController<ProductoDTO>> createProducto(@Valid @RequestBody ProductoDTO producto) {
    	ProductoDTO nuevoProducto = productoService.createProducto(producto);

        ResponseController<ProductoDTO> response = ResponseController.<ProductoDTO>builder()
                .success(true)
                .message("Producto registrado exitosamente")
                .data(nuevoProducto)
                .status(HttpStatus.CREATED.value())        		
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("obtener/{id}")
    public ResponseEntity<ResponseController<Producto>> getProducto(@PathVariable(value = "id", required = true) Long id) {
        return productoService.getProducto(id)
                .map(prod -> ResponseEntity.ok(
                        ResponseController.<Producto>builder()
                                .success(true)
                                .message("Producto encontrado")
                                .data(prod)
                                .status(HttpStatus.OK.value())
                                .build()
                ))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ResponseController.<Producto>builder()
                                .success(false)
                                .message("Producto no encontrado")
                                .status(HttpStatus.NOT_FOUND.value())
                                .build()
                ));
    }
}
