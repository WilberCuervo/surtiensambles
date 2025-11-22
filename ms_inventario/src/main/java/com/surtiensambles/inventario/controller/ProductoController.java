package com.surtiensambles.inventario.controller;

import com.surtiensambles.inventario.entity.Producto;
import com.surtiensambles.inventario.repository.CategoriaRepository;
import com.surtiensambles.inventario.service.ProductoService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;
    private final CategoriaRepository categoriaRepo;

    // Listado paginado y filtrado
    /*
     * este metodo se utiliz
     * 
     * 
     */
    @GetMapping
    public Page<Producto> getAllPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long categoria,
            @RequestParam(required = false) Boolean activo,
            @RequestParam(required = false, defaultValue = "id,asc") String sort
    ) {
        String[] sortParts = sort.split(",");
        Sort.Direction dir = Sort.Direction.ASC;
        String sortField = "id";
        if (sortParts.length >= 1 && !sortParts[0].isBlank()) sortField = sortParts[0];
        if (sortParts.length >= 2 && sortParts[1].equalsIgnoreCase("desc")) dir = Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sortField));

        String safeSearch = (search == null) ? "" : search; // evita null en LOWER()
        return productoService.search(safeSearch, categoria, activo, pageable);
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
    public Producto create(@RequestBody Producto producto) {
        assignCategoria(producto);
        return productoService.save(producto);
    }

    // Actualizar producto
    @PutMapping("/{id}")
    public ResponseEntity<Producto> update(@PathVariable Long id, @RequestBody Producto producto) {
        return productoService.findById(id)
                .map(existing -> {
                    producto.setId(id);
                    assignCategoria(producto);
                    return ResponseEntity.ok(productoService.save(producto));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Eliminar producto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // --- MÉTODO PRIVADO PARA ASIGNAR CATEGORÍA ---
    private void assignCategoria(Producto producto) {
        if (producto.getCategoria() != null && producto.getCategoria().getId() != null) {
            var categoria = categoriaRepo.findById(producto.getCategoria().getId()).orElse(null);
            producto.setCategoria(categoria);
        }
    }
}
