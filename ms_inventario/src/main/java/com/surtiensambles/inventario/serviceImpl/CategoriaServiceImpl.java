package com.surtiensambles.inventario.serviceImpl;

import com.surtiensambles.inventario.dto.PageRequestDto;
import com.surtiensambles.inventario.entity.Categoria;
import com.surtiensambles.inventario.exception.BusinessException;
import com.surtiensambles.inventario.exception.ResourceNotFoundException; 
import com.surtiensambles.inventario.repository.CategoriaRepository;
import com.surtiensambles.inventario.service.CategoriaService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.criteria.Predicate;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository repo;

    /**
     * Implementación centralizada de listado paginado y filtrado.
     * Utiliza PageRequestDto para encapsular todos los parámetros.
     */
    @Override
    public Page<Categoria> listarPaginado(PageRequestDto requestDto) {
        
        // 1. Convertir DTO a objetos Pageable y Sort de Spring
        Sort order = Sort.by(requestDto.getSortDirection(), requestDto.getSortBy());
        Pageable pageable = PageRequest.of(requestDto.getPage(), requestDto.getSize(), order);

        // 2. Construir la Specification dinámica
        Specification<Categoria> spec = buildSpecification(requestDto);

        // 3. Ejecutar la consulta
        return repo.findAll(spec, pageable);
    }

    // Método helper privado para construir la Specification
    private Specification<Categoria> buildSpecification(PageRequestDto requestDto) {
        return (root, query, cb) -> {
            Predicate combinedPredicate = cb.conjunction(); // Empieza con TRUE

            // Filtro por nombre (búsqueda general)
            if (requestDto.getSearch() != null && !requestDto.getSearch().trim().isEmpty()) {
                Predicate searchPredicate = cb.like(cb.lower(root.get("nombre")), 
                                                   "%" + requestDto.getSearch().toLowerCase() + "%");
                combinedPredicate = cb.and(combinedPredicate, searchPredicate);
            }

            // Filtro por estado activo (usa el Boolean nullable del DTO)
            if (requestDto.getActivo() != null) {
                Predicate activoPredicate = cb.equal(root.get("activo"), requestDto.getActivo());
                combinedPredicate = cb.and(combinedPredicate, activoPredicate);
            }
            
            return combinedPredicate;
        };
    }
    
    // Método para endpoint /all con Optional<Boolean> activo
    @Override
    public List<Categoria> listarPorEstado(Optional<Boolean> activo) {
        Specification<Categoria> spec = Specification.where(null);
        
        if (activo.isPresent()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("activo"), activo.get()));
        }
        
        // No paginamos aquí, solo devolvemos una lista simple
        return repo.findAll(spec);
    }


    // Métodos CRUD básicos con Excepciones Personalizadas

    @Override
    public Categoria obtener(Long id) {
      
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + id));
    }

    @Override
    public Categoria crear(Categoria categoria) {
       
        if (repo.existsByNombreIgnoreCase(categoria.getNombre())) {
            throw new BusinessException("La categoría '" + categoria.getNombre() + "' ya existe");
        }
        categoria.setActivo(true);
        return repo.save(categoria);
    }

    @Override
    public Categoria actualizar(Long id, Categoria categoria) {
        Categoria existente = obtener(id); 

        if (!existente.getNombre().equalsIgnoreCase(categoria.getNombre())
                && repo.existsByNombreIgnoreCase(categoria.getNombre())) {
           
            throw new BusinessException("Ya existe otra categoría con el nombre: " + categoria.getNombre());
        }

        existente.setNombre(categoria.getNombre());
        existente.setDescripcion(categoria.getDescripcion());
        existente.setActivo(categoria.getActivo());

        return repo.save(existente);
    }

    @Override
    public void eliminar(Long id) {
        Categoria cat = obtener(id);
        repo.delete(cat);
    }

    @Override
    public boolean existeNombre(String nombre) {
        return repo.existsByNombreIgnoreCase(nombre);
    }

    @Override
    public List<Categoria> listarActivas() {
        
         return listarPorEstado(Optional.of(true));
    }
}