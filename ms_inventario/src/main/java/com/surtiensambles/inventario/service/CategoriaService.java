package com.surtiensambles.inventario.service;

import com.surtiensambles.inventario.entity.Categoria;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CategoriaService {

    Page<Categoria> listar(int page, int size, String search, Boolean activo, String sort);

    Categoria obtener(Long id);

    Categoria crear(Categoria categoria);

    Categoria actualizar(Long id, Categoria categoria);

    void eliminar(Long id);

    boolean existeNombre(String nombre);

    List<Categoria> listarActivas();
}
