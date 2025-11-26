package com.surtiensambles.inventario.serviceImpl;

import com.surtiensambles.inventario.dto.PageRequestDto;
import com.surtiensambles.inventario.entity.Bodega;
import com.surtiensambles.inventario.entity.Producto;
import com.surtiensambles.inventario.entity.Stock;
import com.surtiensambles.inventario.exception.BusinessException;   
import com.surtiensambles.inventario.exception.ResourceNotFoundException;
import com.surtiensambles.inventario.repository.BodegaRepository;
import com.surtiensambles.inventario.repository.ProductoRepository; 
import com.surtiensambles.inventario.repository.StockRepository;
import com.surtiensambles.inventario.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;
    private final ProductoRepository productoRepository;
    private final BodegaRepository bodegaRepository;

    @Override
    public Page<Stock> listarPaginado(PageRequestDto requestDto) {
        Sort order = Sort.by(requestDto.getSortDirection(), requestDto.getSortBy());
        Pageable pageable = PageRequest.of(requestDto.getPage(), requestDto.getSize(), order);
        Specification<Stock> spec = buildSpecification(requestDto);
        return stockRepository.findAll(spec, pageable);
    }

    private Specification<Stock> buildSpecification(PageRequestDto requestDto) {
        return (root, query, cb) -> {
            Predicate combinedPredicate = cb.conjunction();

            if (requestDto.getSearch() != null && !requestDto.getSearch().trim().isEmpty()) {
                String searchLowerCase = "%" + requestDto.getSearch().toLowerCase() + "%";
                
                Join<Stock, Producto> productoJoin = root.join("producto");
                Join<Stock, Bodega> bodegaJoin = root.join("bodega");

                Predicate searchPredicate = cb.or(
                        cb.like(cb.lower(productoJoin.get("nombre")), searchLowerCase),
                        cb.like(cb.lower(productoJoin.get("sku")), searchLowerCase),
                        cb.like(cb.lower(bodegaJoin.get("nombre")), searchLowerCase),
                        cb.like(cb.lower(bodegaJoin.get("codigo")), searchLowerCase));
                
                combinedPredicate = cb.and(combinedPredicate, searchPredicate);
            }
            return combinedPredicate;
        };
    }

    @Override
    public Optional<Stock> findById(Long id) {
        return stockRepository.findById(id);
    }

    // MÉTODOS PARA INTEGRACIÓN CON MOVIMIENTOS

    @Override
    @Transactional
    public void actualizarStock(Long productoId, Long bodegaId, String tipoMovimiento, Integer cantidad) {
        
        // 1. Buscamos el stock, si no existe (primera vez), lo creamos en 0
        Stock stock = stockRepository.findByProductoIdAndBodegaId(productoId, bodegaId)
                .orElseGet(() -> crearStockVacio(productoId, bodegaId));

        // 2. Lógica de actualización según el tipo de movimiento
        switch (tipoMovimiento) {
            case "ENTRADA":
                stock.setCantidadDisponible(stock.getCantidadDisponible() + cantidad);
                break;
                
            case "SALIDA":
                validarStockSuficiente(stock, cantidad);
                stock.setCantidadDisponible(stock.getCantidadDisponible() - cantidad);
                break;
                
            case "AJUSTE":
                stock.setCantidadDisponible(stock.getCantidadDisponible() + cantidad);
                break;
                
            default:

                throw new IllegalArgumentException("Acción de stock no soportada: " + tipoMovimiento);
        }

        stock.setUltimaActualizacion(LocalDateTime.now());
        stockRepository.save(stock);
    }

    private Stock crearStockVacio(Long productoId, Long bodegaId) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado (ID: " + productoId + ")"));
        
        Bodega bodega = bodegaRepository.findById(bodegaId)
                .orElseThrow(() -> new ResourceNotFoundException("Bodega no encontrada (ID: " + bodegaId + ")"));

        return Stock.builder()
                .producto(producto)
                .bodega(bodega)
                .cantidadDisponible(0)
                .cantidadReservada(0)
                .ultimaActualizacion(LocalDateTime.now())
                .build();
    }

    private void validarStockSuficiente(Stock stock, Integer cantidadRequerida) {
        if (stock.getCantidadDisponible() < cantidadRequerida) {
         
            throw new BusinessException("Stock insuficiente en '" + stock.getBodega().getNombre() + 
                    "'. Disponible: " + stock.getCantidadDisponible() + ", Requerido: " + cantidadRequerida);
        }
    }
    
    @Override
    @Transactional
    public void reservarStock(Long productoId, Long bodegaId, Integer cantidad) {
        Stock stock = stockRepository.findByProductoIdAndBodegaId(productoId, bodegaId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe registro de stock para este producto en la bodega seleccionada"));

        if (stock.getCantidadDisponible() < cantidad) {
           
            throw new BusinessException("Stock insuficiente para reservar. Disponible: " + stock.getCantidadDisponible());
        }

        // Mueve de Disponible -> Reservado
        stock.setCantidadDisponible(stock.getCantidadDisponible() - cantidad);
        stock.setCantidadReservada(stock.getCantidadReservada() + cantidad);
        stock.setUltimaActualizacion(LocalDateTime.now());
        
        stockRepository.save(stock);
    }

    @Override
    @Transactional
    public void liberarReserva(Long productoId, Long bodegaId, Integer cantidad) {
        Stock stock = stockRepository.findByProductoIdAndBodegaId(productoId, bodegaId)
                .orElseThrow(() -> new ResourceNotFoundException("Stock no encontrado para liberar reserva"));

        // Mueve de Reservado -> Disponible (El cliente canceló)
        stock.setCantidadReservada(stock.getCantidadReservada() - cantidad);
        stock.setCantidadDisponible(stock.getCantidadDisponible() + cantidad);
        stock.setUltimaActualizacion(LocalDateTime.now());

        stockRepository.save(stock);
    }

    @Override
    @Transactional
    public void descontarDeReserva(Long productoId, Long bodegaId, Integer cantidad) {
        Stock stock = stockRepository.findByProductoIdAndBodegaId(productoId, bodegaId)
                .orElseThrow(() -> new ResourceNotFoundException("Stock no encontrado para confirmar reserva"));

        // Saca de Reservado -> Afuera (Se concretó la venta)
        if (stock.getCantidadReservada() < cantidad) {
             // CAMBIO: BusinessException (400)
             throw new BusinessException("Inconsistencia crítica: No hay suficiente stock reservado para descontar.");
        }
        
        stock.setCantidadReservada(stock.getCantidadReservada() - cantidad);
        stock.setUltimaActualizacion(LocalDateTime.now());

        stockRepository.save(stock);
    }
    
    @Override
    public List<Stock> obtenerReporteReabastecimiento(Long bodegaId) {
        if (bodegaId != null) {
            return stockRepository.findProductosBajosDeStockPorBodega(bodegaId);
        }
        return stockRepository.findProductosBajosDeStock();
    }
}