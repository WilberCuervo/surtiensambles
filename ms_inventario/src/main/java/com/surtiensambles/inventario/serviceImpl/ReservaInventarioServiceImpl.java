package com.surtiensambles.inventario.serviceImpl;

import com.surtiensambles.inventario.dto.ReservaRequestDto;
import com.surtiensambles.inventario.entity.*;
import com.surtiensambles.inventario.exception.BusinessException;       // <--- IMPORTANTE
import com.surtiensambles.inventario.exception.ResourceNotFoundException; // <--- IMPORTANTE
import com.surtiensambles.inventario.repository.*;
import com.surtiensambles.inventario.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservaInventarioServiceImpl {

    private final ReservaInventarioRepository reservaRepository;
    private final ProductoRepository productoRepository;
    private final BodegaRepository bodegaRepository;
    private final StockService stockService;
    private final MovimientoInventarioRepository movimientoRepository; 

    @Transactional
    public ReservaInventario crearReserva(ReservaRequestDto dto) {
        // 1. Validar existencias usando ResourceNotFoundException
        Producto producto = productoRepository.findById(dto.getProductoId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + dto.getProductoId()));
        
        Bodega bodega = bodegaRepository.findById(dto.getBodegaId())
                .orElseThrow(() -> new ResourceNotFoundException("Bodega no encontrada con ID: " + dto.getBodegaId()));

        // 2. Bloquear Stock (Mover de Disponible a Reservado)
        // Nota: stockService.reservarStock ya lanza sus propias excepciones si falla
        stockService.reservarStock(producto.getId(), bodega.getId(), dto.getCantidad());

        // 3. Crear registro de Reserva
        ReservaInventario reserva = new ReservaInventario();
        reserva.setProducto(producto);
        reserva.setBodega(bodega);
        reserva.setCantidad(dto.getCantidad());
        reserva.setFechaReserva(LocalDateTime.now());
        reserva.setFechaExpiracion(dto.getFechaExpiracion());
        reserva.setEstado(EstadoReserva.ACTIVA);
        reserva.setNota(dto.getNota());
        reserva.setUsuario(dto.getUsuario());
        
        String ref = "RES-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        reserva.setReferenciaReserva(ref);

        return reservaRepository.save(reserva);
    }

    @Transactional
    public void cancelarReserva(Long reservaId) {
        ReservaInventario reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con ID: " + reservaId));

        // Validación de Regla de Negocio
        if (reserva.getEstado() != EstadoReserva.ACTIVA) {
            throw new BusinessException("Solo se pueden cancelar reservas que estén en estado ACTIVA.");
        }

        // 1. Devolver Stock
        stockService.liberarReserva(reserva.getProducto().getId(), reserva.getBodega().getId(), reserva.getCantidad());

        // 2. Actualizar estado
        reserva.setEstado(EstadoReserva.CANCELADA);
        reserva.setFechaExpiracion(LocalDateTime.now()); 
        reservaRepository.save(reserva);
    }

    @Transactional
    public void confirmarVentaReserva(Long reservaId) {
        ReservaInventario reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con ID: " + reservaId));

        // Validación de Regla de Negocio
        if (reserva.getEstado() != EstadoReserva.ACTIVA) {
            throw new BusinessException("No se puede confirmar la venta. La reserva no está ACTIVA (Estado actual: " + reserva.getEstado() + ")");
        }

        // 1. Descontar definitivamente del Stock Reservado
        stockService.descontarDeReserva(reserva.getProducto().getId(), reserva.getBodega().getId(), reserva.getCantidad());

        // 2. Registrar en el Historial
        MovimientoInventario mov = new MovimientoInventario();
        mov.setProducto(reserva.getProducto());
        mov.setBodega(reserva.getBodega());
        mov.setTipo("SALIDA");
        mov.setCantidad(reserva.getCantidad());
        mov.setFecha(LocalDateTime.now());
        mov.setReferenciaTipo("RESERVA_CONFIRMADA");
        mov.setReferenciaId(reserva.getReferenciaReserva());
        mov.setNota("Venta de reserva confirmada. Cliente: " + reserva.getNota());
        mov.setUsuario(reserva.getUsuario());
        
        movimientoRepository.save(mov);

        // 3. Actualizar estado de la reserva
        reserva.setEstado(EstadoReserva.CONSUMIDA);
        reservaRepository.save(reserva);
    }
    	
    public List<ReservaInventario> listarReservasActivas(Long bodegaId) {
    	if (bodegaId != null) {
            return reservaRepository.findByEstadoAndBodegaId(EstadoReserva.ACTIVA, bodegaId);
    	}
    	return reservaRepository.findByEstado(EstadoReserva.ACTIVA);
    }
}