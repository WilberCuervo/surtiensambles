package com.surtiensambles.inventario.serviceImpl;

import com.surtiensambles.inventario.dto.MovimientoRequestDto;
import com.surtiensambles.inventario.dto.PageRequestDto;
import com.surtiensambles.inventario.entity.*;
import com.surtiensambles.inventario.repository.*;
import com.surtiensambles.inventario.service.MovimientoInventarioService;
import com.surtiensambles.inventario.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MovimientoInventarioServiceImpl implements MovimientoInventarioService {

    private final MovimientoInventarioRepository movimientoRepository;
    private final ProductoRepository productoRepository;
    private final BodegaRepository bodegaRepository;
    
    // Inyectamos el servicio de stock que acabamos de modificar
    private final StockService stockService;

    @Override
    @Transactional
    public MovimientoInventario registrarMovimiento(MovimientoRequestDto dto) {
        
        // Caso especial: TRANSFERENCIA (Mueve de Bodega A a Bodega B)
        if ("TRANSFERENCIA".equalsIgnoreCase(dto.getTipo())) {
            return procesarTransferencia(dto);
        }

        // Lógica estándar para ENTRADA, SALIDA, AJUSTE
        return procesarMovimientoSimple(dto);
    }

    private MovimientoInventario procesarMovimientoSimple(MovimientoRequestDto dto) {
        // 1. Validaciones básicas
        Producto producto = productoRepository.findById(dto.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        Bodega bodega = bodegaRepository.findById(dto.getBodegaId())
                .orElseThrow(() -> new RuntimeException("Bodega no encontrada"));

        // 2. Actualizar Stock (Delegamos la responsabilidad al StockService)
        stockService.actualizarStock(producto.getId(), bodega.getId(), dto.getTipo(), dto.getCantidad());

        // 3. Guardar el registro del movimiento
        MovimientoInventario mov = new MovimientoInventario();
        mov.setProducto(producto);
        mov.setBodega(bodega);
        mov.setTipo(dto.getTipo());
        mov.setCantidad(dto.getCantidad());
        mov.setFecha(LocalDateTime.now());
        mov.setNota(dto.getNota());
        mov.setUsuario(dto.getUsuario());
        mov.setReferenciaTipo(dto.getReferenciaTipo());
        mov.setReferenciaId(dto.getReferenciaId());

        return movimientoRepository.save(mov);
    }

    private MovimientoInventario procesarTransferencia(MovimientoRequestDto dto) {
        // Generamos un ID único para agrupar la salida y la entrada
        String trasladoId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // 1. Registrar SALIDA de Bodega Origen
        MovimientoRequestDto salidaDto = new MovimientoRequestDto();
        salidaDto.setProductoId(dto.getProductoId());
        salidaDto.setBodegaId(dto.getBodegaOrigenId());
        salidaDto.setTipo("SALIDA"); // A efectos de stock, es una salida
        salidaDto.setCantidad(dto.getCantidad());
        salidaDto.setNota("Traslado a Bodega ID: " + dto.getBodegaDestinoId() + ". " + dto.getNota());
        salidaDto.setReferenciaTipo("TRASLADO_SALIDA");
        salidaDto.setReferenciaId(trasladoId);
        salidaDto.setUsuario(dto.getUsuario());
        
        // Procesamos la salida (esto validará stock y restará)
        procesarMovimientoSimple(salidaDto);

        // 2. Registrar ENTRADA a Bodega Destino
        MovimientoRequestDto entradaDto = new MovimientoRequestDto();
        entradaDto.setProductoId(dto.getProductoId());
        entradaDto.setBodegaId(dto.getBodegaDestinoId());
        entradaDto.setTipo("ENTRADA"); // A efectos de stock, es una entrada
        entradaDto.setCantidad(dto.getCantidad());
        entradaDto.setNota("Traslado desde Bodega ID: " + dto.getBodegaOrigenId() + ". " + dto.getNota());
        entradaDto.setReferenciaTipo("TRASLADO_ENTRADA");
        entradaDto.setReferenciaId(trasladoId);
        entradaDto.setUsuario(dto.getUsuario());

        // Procesamos la entrada (esto sumará stock o creará registro)
        return procesarMovimientoSimple(entradaDto);
    }

    @Override
    public Page<MovimientoInventario> listarPaginado(PageRequestDto requestDto) {
        Sort order = Sort.by(requestDto.getSortDirection(), requestDto.getSortBy());
        Pageable pageable = PageRequest.of(requestDto.getPage(), requestDto.getSize(), order);
        Specification<MovimientoInventario> spec = buildSpecification(requestDto);
        return movimientoRepository.findAll(spec, pageable);
    }

    private Specification<MovimientoInventario> buildSpecification(PageRequestDto requestDto) {
        return (root, query, cb) -> {
            Predicate combinedPredicate = cb.conjunction();

            // 1. Filtro General (Search)
            if (requestDto.getSearch() != null && !requestDto.getSearch().isEmpty()) {
                String term = "%" + requestDto.getSearch().toLowerCase() + "%";
                Predicate prodName = cb.like(cb.lower(root.get("producto").get("nombre")), term);
                Predicate bodegaName = cb.like(cb.lower(root.get("bodega").get("nombre")), term);
                Predicate tipo = cb.like(cb.lower(root.get("tipo")), term);	
                
                combinedPredicate = cb.and(combinedPredicate, cb.or(prodName, bodegaName, tipo));
            }

            // 2. Filtro Fecha Inicio (Desde las 00:00:00 del día seleccionado)
            if (requestDto.getFechaInicio() != null) {
                // "fecha" >= fechaInicio 00:00
                Predicate fechaInicioPred = cb.greaterThanOrEqualTo(
                    root.get("fecha"), 
                    requestDto.getFechaInicio().atStartOfDay()
                );
                combinedPredicate = cb.and(combinedPredicate, fechaInicioPred);
            }

            // 3. Filtro Fecha Fin (Hasta las 23:59:59 del día seleccionado)
            if (requestDto.getFechaFin() != null) {
                // "fecha" <= fechaFin 23:59:59.999999
                Predicate fechaFinPred = cb.lessThanOrEqualTo(
                    root.get("fecha"), 
                    requestDto.getFechaFin().atTime(23, 59, 59)
                );
                combinedPredicate = cb.and(combinedPredicate, fechaFinPred);
            }

            return combinedPredicate;
        };
    }
}