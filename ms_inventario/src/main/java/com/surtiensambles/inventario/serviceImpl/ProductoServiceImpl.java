package com.surtiensambles.inventario.serviceImpl;

import com.surtiensambles.inventario.entity.Producto;
import com.surtiensambles.inventario.repository.ProductoRepository;
import com.surtiensambles.inventario.service.ProductoService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    @Override
    public Producto save(Producto producto) {
        return productoRepository.save(producto);
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
        productoRepository.deleteById(id);
    }

    @Override
    public Page<Producto> search(String search, Long categoriaId, Boolean activo, Pageable pageable) {
        String safeSearch = (search == null) ? "" : search; // evita null en la consulta
        return productoRepository.search(safeSearch, categoriaId, activo, pageable);
    }
}