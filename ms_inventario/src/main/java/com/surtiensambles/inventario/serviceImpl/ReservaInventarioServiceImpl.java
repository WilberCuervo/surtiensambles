package com.surtiensambles.inventario.serviceImpl;

import com.surtiensambles.inventario.dto.ReservaRequestDto;
import com.surtiensambles.inventario.entity.*;
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
    private final MovimientoInventarioRepository movimientoRepository; // Para registrar la salida final

    @Transactional
    public ReservaInventario crearReserva(ReservaRequestDto dto) {
        // 1. Validar existencias
        Producto producto = productoRepository.findById(dto.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        Bodega bodega = bodegaRepository.findById(dto.getBodegaId())
                .orElseThrow(() -> new RuntimeException("Bodega no encontrada"));

        // 2. Bloquear Stock (Mover de Disponible a Reservado)
        stockService.reservarStock(producto.getId(), bodega.getId(), dto.getCantidad());

        // 3. Crear registro de Reserva
        ReservaInventario reserva = new ReservaInventario();
        reserva.setProducto(producto);
        reserva.setBodega(bodega);
        reserva.setCantidad(dto.getCantidad());
        reserva.setFechaReserva(LocalDateTime.now());
        reserva.setFechaExpiracion(dto.getFechaExpiracion()); // Puede ser null
        reserva.setEstado(EstadoReserva.ACTIVA);
        reserva.setNota(dto.getNota());
        reserva.setUsuario(dto.getUsuario());
        
        // Generar un código único corto (Ej: RES-A1B2)
        String ref = "RES-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        reserva.setReferenciaReserva(ref);

        return reservaRepository.save(reserva);
    }

    @Transactional
    public void cancelarReserva(Long reservaId) {
        ReservaInventario reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        if (reserva.getEstado() != EstadoReserva.ACTIVA) {
            throw new RuntimeException("Solo se pueden cancelar reservas ACTIVAS");
        }

        // 1. Devolver Stock (Mover de Reservado a Disponible)
        stockService.liberarReserva(reserva.getProducto().getId(), reserva.getBodega().getId(), reserva.getCantidad());

        // 2. Actualizar estado
        reserva.setEstado(EstadoReserva.CANCELADA);
        reserva.setFechaExpiracion(LocalDateTime.now()); // Fecha de cancelación
        reservaRepository.save(reserva);
    }

    @Transactional
    public void confirmarVentaReserva(Long reservaId) {
        ReservaInventario reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        if (reserva.getEstado() != EstadoReserva.ACTIVA) {
            throw new RuntimeException("Solo se pueden confirmar reservas ACTIVAS");
        }

        // 1. Descontar definitivamente del Stock Reservado
        stockService.descontarDeReserva(reserva.getProducto().getId(), reserva.getBodega().getId(), reserva.getCantidad());

        // 2. Registrar en el Historial
        // OJO: No usamos movimientoService.registrar() porque ese volvería a descontar stock.
        // Guardamos el movimiento manualmente solo para historial.
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
    	
    	// Listar reservas activas
    	public List<ReservaInventario> listarReservasActivas(Long bodegaId) {
    		
    	if (bodegaId != null) {
    			
            // Si nos piden una bodega específica
         return reservaRepository.findByEstadoAndBodegaId(EstadoReserva.ACTIVA, bodegaId);
           
    		}
        // Si no especifican bodega, traemos todas las activas
    	return reservaRepository.findByEstado(EstadoReserva.ACTIVA);
    }
}