package com.surtiensambles.inventario.serviceImpl;

import com.surtiensambles.inventario.entity.Bodega;
import com.surtiensambles.inventario.repository.BodegaRepository;
import com.surtiensambles.inventario.service.BodegaService;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BodegaServiceImpl implements BodegaService {

    private final BodegaRepository bodegaRepository;

    public BodegaServiceImpl(BodegaRepository bodegaRepository) {
        this.bodegaRepository = bodegaRepository;
    }

    @Override
    public Bodega save(Bodega bodega) {
        return bodegaRepository.save(bodega);
    }

    @Override
    public List<Bodega> findAll() {
        return bodegaRepository.findAll();
    }

    @Override
    public Optional<Bodega> findById(Long id) {
        return bodegaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        bodegaRepository.deleteById(id);
    }
}
