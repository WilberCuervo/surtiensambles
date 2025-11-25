package com.surtiensambles.inventario.serviceImpl;

import com.surtiensambles.inventario.dto.PageRequestDto;
import com.surtiensambles.inventario.entity.Producto;
import com.surtiensambles.inventario.repository.MovimientoInventarioRepository;
import com.surtiensambles.inventario.repository.ProductoRepository;
import com.surtiensambles.inventario.repository.StockRepository;
import com.surtiensambles.inventario.service.ProductoService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.criteria.Predicate;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final StockRepository stockRepository;         
    private final MovimientoInventarioRepository movimientoRepository;

    /**
     * Implementación centralizada de listado paginado y filtrado para Productos.
     */
    @Override
    public Page<Producto> listarPaginado(PageRequestDto requestDto) {
        
        // 1. Convertir DTO a objetos Pageable y Sort de Spring
        Sort order = Sort.by(requestDto.getSortDirection(), requestDto.getSortBy());
        Pageable pageable = PageRequest.of(requestDto.getPage(), requestDto.getSize(), order);

        // 2. Construir la Specification dinámica (incluyendo filtro de categoría si lo añades al DTO)
        Specification<Producto> spec = buildSpecification(requestDto);

        // 3. Ejecutar la consulta
        return productoRepository.findAll(spec, pageable);
    }

    /**
     * Método helper privado para construir la Specification dinámica de Producto.
     * Aquí manejamos la búsqueda por nombre/sku, por estado activo o categoria.
     */
    private Specification<Producto> buildSpecification(PageRequestDto requestDto) {
        return (root, query, cb) -> {
            Predicate combinedPredicate = cb.conjunction();

            // Filtro por nombre o SKU (búsqueda general)
            if (requestDto.getSearch() != null && !requestDto.getSearch().trim().isEmpty()) {
                String searchLowerCase = "%" + requestDto.getSearch().toLowerCase() + "%";
                Predicate nombrePredicate = cb.like(cb.lower(root.get("nombre")), searchLowerCase);
                Predicate skuPredicate = cb.like(cb.lower(root.get("sku")), searchLowerCase);
                
                // Combina OR (nombre OR sku)
                Predicate searchPredicate = cb.or(nombrePredicate, skuPredicate);
                combinedPredicate = cb.and(combinedPredicate, searchPredicate);
            }

            // Filtro por estado activo
            if (requestDto.getActivo() != null) {
                Predicate activoPredicate = cb.equal(root.get("activo"), requestDto.getActivo());
                combinedPredicate = cb.and(combinedPredicate, activoPredicate);
            }
            
            // Filtro por categoriaId
            if (requestDto.getCategoriaId() != null) {
                // Navegamos de Producto a Categoria y comparamos el ID
                Predicate categoriaPredicate = cb.equal(root.get("categoria").get("id"), requestDto.getCategoriaId());
                combinedPredicate = cb.and(combinedPredicate, categoriaPredicate);
            }

            return combinedPredicate;
        };
    }
    
    // Método para endpoint /all con Optional<Boolean> activo
    @Override
    public List<Producto> listarPorEstado(Optional<Boolean> activo) {
        Specification<Producto> spec = Specification.where(null);
        
        if (activo.isPresent()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("activo"), activo.get()));
        }
        
        return productoRepository.findAll(spec);
    }


    @Override
    public Producto crear(Producto producto) {
        if (productoRepository.existsBySku(producto.getSku())) {
            throw new RuntimeException("El SKU del producto ya existe");
        }
        producto.setCreatedAt(LocalDateTime.now());
        //producto.setActivo(true);
        return productoRepository.save(producto);
    }
    
    @Override
    public Producto actualizar(Long id, Producto producto) {
        Producto existente = findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        existente.setNombre(producto.getNombre());
        existente.setDescripcion(producto.getDescripcion());
        existente.setActivo(producto.getActivo());
        existente.setCategoria(producto.getCategoria());
        existente.setUpdatedAt(LocalDateTime.now());

        existente.setSku(producto.getSku());
        existente.setPrecioReferencia(producto.getPrecioReferencia());
        existente.setUnidadMedida(producto.getUnidadMedida());
        existente.setNivelReorden(producto.getNivelReorden());
        // ---------------------------------------
        
        return productoRepository.save(existente);
    }

    @Override
    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    @Override
    public Optional<Producto> findById(Long id) {
        return productoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // PASO 1: Verificar si tiene relaciones importantes (Stock o Movimientos)
        // Para saber esto, necesitamos preguntar a los repositorios si existen registros
        boolean tieneStock = stockRepository.existsByProductoId(id);
        boolean tieneMovimientos = movimientoRepository.existsByProductoId(id);

        if (tieneStock || tieneMovimientos) {
        	
            // Si ya tuvo vida en el sistema, solo lo inactivamos.
            producto.setActivo(false);
            producto.setUpdatedAt(LocalDateTime.now());
            productoRepository.save(producto);
            
        } else {
        	
            // Si es un producto nuevo que crearon por error y nunca se usó, lo borramos de verdad.
            productoRepository.delete(producto);
        }
    }
    
    @Override
    public boolean existsBySku(String sku) {
        return productoRepository.existsBySku(sku);
    }
}
