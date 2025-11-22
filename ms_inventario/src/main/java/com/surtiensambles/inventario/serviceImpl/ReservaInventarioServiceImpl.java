package com.surtiensambles.inventario.serviceImpl;

import com.surtiensambles.inventario.entity.ReservaInventario;
import com.surtiensambles.inventario.repository.ReservaInventarioRepository;
import com.surtiensambles.inventario.service.ReservaInventarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservaInventarioServiceImpl implements ReservaInventarioService {

    private final ReservaInventarioRepository reservaRepository;

    @Override
    public ReservaInventario crearReserva(ReservaInventario reserva) {
        // Estado por defecto: ACTIVA
        reserva.setEstado("ACTIVA");
        if (reserva.getFechaReserva() == null) {
            reserva.setFechaReserva(LocalDateTime.now());
        }
        return reservaRepository.save(reserva);
    }

    @Override
    public ReservaInventario consumirReserva(String referencia) {
        ReservaInventario reserva = reservaRepository.findByReferenciaReserva(referencia)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada: " + referencia));
        if (!"ACTIVA".equals(reserva.getEstado())) {
            throw new RuntimeException("Reserva no está activa: " + referencia);
        }
        reserva.setEstado("CONSUMIDA");
        return reservaRepository.save(reserva);
    }

    @Override
    public ReservaInventario cancelarReserva(String referencia) {
        ReservaInventario reserva = reservaRepository.findByReferenciaReserva(referencia)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada: " + referencia));
        if (!"ACTIVA".equals(reserva.getEstado())) {
            throw new RuntimeException("Solo reservas activas pueden cancelarse: " + referencia);
        }
        reserva.setEstado("CANCELADA");
        return reservaRepository.save(reserva);
    }

    @Override
    public List<ReservaInventario> listarReservas() {
        return reservaRepository.findAll();
    }
}
