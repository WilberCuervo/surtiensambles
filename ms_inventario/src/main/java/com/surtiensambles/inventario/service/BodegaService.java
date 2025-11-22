package com.surtiensambles.inventario.service;

import java.util.List;
import java.util.Optional;

import com.surtiensambles.inventario.entity.Bodega;

public interface BodegaService {
    Bodega save(Bodega bodega);
    List<Bodega> findAll();
    Optional<Bodega> findById(Long id);
    void delete(Long id);
}
