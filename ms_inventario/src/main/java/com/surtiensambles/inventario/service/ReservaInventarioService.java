package com.surtiensambles.inventario.service;

import com.surtiensambles.inventario.entity.ReservaInventario;

import java.util.List;

public interface ReservaInventarioService {

    ReservaInventario crearReserva(ReservaInventario reserva);

    ReservaInventario consumirReserva(String referencia);

    ReservaInventario cancelarReserva(String referencia);

    List<ReservaInventario> listarReservas();
}
