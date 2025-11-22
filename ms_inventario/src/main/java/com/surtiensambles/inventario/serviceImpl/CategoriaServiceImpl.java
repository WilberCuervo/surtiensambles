package com.surtiensambles.inventario.serviceImpl;

import com.surtiensambles.inventario.entity.Categoria;
import com.surtiensambles.inventario.repository.CategoriaRepository;
import com.surtiensambles.inventario.service.CategoriaService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository repo;

    @Override
    public Page<Categoria> listar(int page, int size, String search, Boolean activo, String sort) {

        String[] sortParts = sort.split(",");
        Sort order = Sort.by(
                sortParts.length == 2
                        ? Sort.Direction.fromString(sortParts[1])
                        : Sort.Direction.ASC,
                sortParts[0]
        );

        Pageable pageable = PageRequest.of(page, size, order);

        Specification<Categoria> spec = Specification.where(null);

        // filtro por nombre
        if (search != null && !search.trim().isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("nombre")), "%" + search.toLowerCase() + "%")
            );
        }

        // filtro por activo
        if (activo != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("activo"), activo)
            );
        }

        return repo.findAll(spec, pageable);
    }

    @Override
    public Categoria obtener(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
    }

    @Override
    public Categoria crear(Categoria categoria) {

        if (repo.existsByNombreIgnoreCase(categoria.getNombre())) {
            throw new RuntimeException("La categoría ya existe");
        }

        categoria.setActivo(true);
        return repo.save(categoria);
    }

    @Override
    public Categoria actualizar(Long id, Categoria categoria) {

        Categoria existente = obtener(id);

        if (!existente.getNombre().equalsIgnoreCase(categoria.getNombre())
                && repo.existsByNombreIgnoreCase(categoria.getNombre())) {
            throw new RuntimeException("Ya existe otra categoría con ese nombre");
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
        return repo.findAll().stream()
                .filter(Categoria::getActivo)
                .toList();
    }
}
