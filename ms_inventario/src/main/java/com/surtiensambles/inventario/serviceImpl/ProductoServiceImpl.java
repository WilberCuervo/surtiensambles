package com.surtiensambles.inventario.serviceImpl;

import com.surtiensambles.inventario.dto.PageRequestDto;
import com.surtiensambles.inventario.entity.Producto;
import com.surtiensambles.inventario.exception.BusinessException;
import com.surtiensambles.inventario.exception.ResourceNotFoundException;
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

    @Override
    public Page<Producto> listarPaginado(PageRequestDto requestDto) {
        Sort order = Sort.by(requestDto.getSortDirection(), requestDto.getSortBy());
        Pageable pageable = PageRequest.of(requestDto.getPage(), requestDto.getSize(), order);
        Specification<Producto> spec = buildSpecification(requestDto);
        return productoRepository.findAll(spec, pageable);
    }

    private Specification<Producto> buildSpecification(PageRequestDto requestDto) {
        return (root, query, cb) -> {
            Predicate combinedPredicate = cb.conjunction();

            if (requestDto.getSearch() != null && !requestDto.getSearch().trim().isEmpty()) {
                String searchLowerCase = "%" + requestDto.getSearch().toLowerCase() + "%";
                Predicate nombrePredicate = cb.like(cb.lower(root.get("nombre")), searchLowerCase);
                Predicate skuPredicate = cb.like(cb.lower(root.get("sku")), searchLowerCase);
                combinedPredicate = cb.and(combinedPredicate, cb.or(nombrePredicate, skuPredicate));
            }

            if (requestDto.getActivo() != null) {
                combinedPredicate = cb.and(combinedPredicate, cb.equal(root.get("activo"), requestDto.getActivo()));
            }
            
            if (requestDto.getCategoriaId() != null) {
                combinedPredicate = cb.and(combinedPredicate, cb.equal(root.get("categoria").get("id"), requestDto.getCategoriaId()));
            }

            return combinedPredicate;
        };
    }
    
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
            throw new BusinessException("Ya existe un producto con el SKU: " + producto.getSku());
        }
        producto.setCreatedAt(LocalDateTime.now());
        return productoRepository.save(producto);
    }
    
    @Override
    public Producto actualizar(Long id, Producto producto) {
        Producto existente = findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));
        
        existente.setNombre(producto.getNombre());
        existente.setDescripcion(producto.getDescripcion());
        existente.setActivo(producto.getActivo());
        existente.setCategoria(producto.getCategoria());
        existente.setUpdatedAt(LocalDateTime.now());

        existente.setSku(producto.getSku());
        existente.setPrecioReferencia(producto.getPrecioReferencia());
        existente.setUnidadMedida(producto.getUnidadMedida());
        existente.setNivelReorden(producto.getNivelReorden());
        
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
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));

        boolean tieneStock = stockRepository.existsByProductoId(id);
        boolean tieneMovimientos = movimientoRepository.existsByProductoId(id);

        if (tieneStock || tieneMovimientos) {
            // Borrado Lógico
            producto.setActivo(false);
            producto.setUpdatedAt(LocalDateTime.now());
            productoRepository.save(producto);
        } else {
            // Borrado Físico
            productoRepository.delete(producto);
        }
    }
    
    @Override
    public boolean existsBySku(String sku) {
        return productoRepository.existsBySku(sku);
    }
}