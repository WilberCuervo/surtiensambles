package com.surtiensambles.inventario.service;

import com.surtiensambles.inventario.dto.PageRequestDto;
import com.surtiensambles.inventario.entity.Categoria;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface CategoriaService {

    // Método principal para listar paginado y filtrado usando el DTO
    Page<Categoria> listarPaginado(PageRequestDto requestDto); 

    // Métodos CRUD básicos
    Categoria obtener(Long id);

    Categoria crear(Categoria categoria);

    Categoria actualizar(Long id, Categoria categoria);

    void eliminar(Long id);

    // Métodos auxiliares
    boolean existeNombre(String nombre);

    List<Categoria> listarActivas();
    
    // Opcional: Nuevo método para el endpoint /all que acepte un booleano opcional
    List<Categoria> listarPorEstado(Optional<Boolean> activo);
}
