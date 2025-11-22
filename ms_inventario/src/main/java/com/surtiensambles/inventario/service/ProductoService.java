package com.surtiensambles.inventario.service;

import com.surtiensambles.inventario.entity.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductoService {

    Producto save(Producto producto);

    List<Producto> findAll();

    Optional<Producto> findById(Long id);

    void delete(Long id);

    Page<Producto> search(String search, Long categoriaId, Boolean activo, Pageable pageable);
}
