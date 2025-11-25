package com.surtiensambles.inventario.controller;

import com.surtiensambles.inventario.dto.PageRequestDto;
import com.surtiensambles.inventario.entity.Producto;
import com.surtiensambles.inventario.repository.CategoriaRepository;
import com.surtiensambles.inventario.service.ProductoService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;
    private final CategoriaRepository categoriaRepo;

    
    // Endpoint principal para listado paginado y filtrado de productos.
    @GetMapping
    public ResponseEntity<Page<Producto>> getAllPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam(required = false) Long categoriaId, 
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
        requestDto.setCategoriaId(categoriaId);
        requestDto.setSortBy(sortBy);
        requestDto.setSortDirection(sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC);

        Page<Producto> pagina = productoService.listarPaginado(requestDto);
        return ResponseEntity.ok(pagina);
    }
    
    /**
     * Endpoint para obtener TODOS los productos (para dropdowns/selects u otras listas).
     * Acepta un parámetro 'activo' opcional para filtrar.
     */
    @GetMapping("/all")
    public ResponseEntity<List<Producto>> listarTodosOporEstado(@RequestParam Optional<Boolean> activo) {
        List<Producto> productos = productoService.listarPorEstado(activo);
        return ResponseEntity.ok(productos);
    }


    // Obtener producto por id
    @GetMapping("/{id}")
    public ResponseEntity<Producto> getById(@PathVariable Long id) {
        return productoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Crear producto
    @PostMapping
    public ResponseEntity<Producto> create(@RequestBody Producto producto) {
        assignCategoria(producto);
        Producto nuevoProducto = productoService.crear(producto); 
        return ResponseEntity.status(201).body(nuevoProducto);
    }

    // Actualizar producto
    @PutMapping("/{id}")
    public ResponseEntity<Producto> update(@PathVariable Long id, @RequestBody Producto producto) {
        
        Producto productoActualizado = productoService.actualizar(id, producto); 
       
        return ResponseEntity.ok(productoActualizado);
    }

    // Eliminar producto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private void assignCategoria(Producto producto) {
        if (producto.getCategoria() != null && producto.getCategoria().getId() != null) {
            var categoria = categoriaRepo.findById(producto.getCategoria().getId()).orElse(null);
            producto.setCategoria(categoria);
        }
    }
}
