package com.surtiensambles.inventario.serviceImpl;

import com.surtiensambles.inventario.dto.PageRequestDto;
import com.surtiensambles.inventario.entity.Proveedor;
import com.surtiensambles.inventario.repository.ProveedorRepository;
import com.surtiensambles.inventario.service.ProveedorService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.criteria.Predicate;

@Service
@RequiredArgsConstructor
public class ProveedorServiceImpl implements ProveedorService {

	private final ProveedorRepository repo;

    /**
     * Implementación centralizada de listado paginado y filtrado para Proveedores.
     */
	@Override
	public Page<Proveedor> listarPaginado(PageRequestDto requestDto) {

		// 1. Convertir DTO a objetos Pageable y Sort de Spring
		Sort order = Sort.by(requestDto.getSortDirection(), requestDto.getSortBy());
		Pageable pageable = PageRequest.of(requestDto.getPage(), requestDto.getSize(), order);

		// 2. Construir la Specification dinámica
		Specification<Proveedor> spec = buildSpecification(requestDto);

		// 3. Ejecutar la consulta
		return repo.findAll(spec, pageable);
	}

    /**
     * Método helper privado para construir la Specification dinámica de Proveedor.
     * Maneja la búsqueda por nombre/nit y por estado activo.
     */
    private Specification<Proveedor> buildSpecification(PageRequestDto requestDto) {
        return (root, query, cb) -> {
            Predicate combinedPredicate = cb.conjunction();

            // Filtro por nombre o NIT (búsqueda general)
            if (requestDto.getSearch() != null && !requestDto.getSearch().trim().isEmpty()) {
                String searchLowerCase = "%" + requestDto.getSearch().toLowerCase() + "%";
                Predicate nombrePredicate = cb.like(cb.lower(root.get("nombre")), searchLowerCase);
                Predicate nitPredicate = cb.like(cb.lower(root.get("nit")), searchLowerCase);
                
                // Combina OR (nombre OR nit)
                Predicate searchPredicate = cb.or(nombrePredicate, nitPredicate);
                combinedPredicate = cb.and(combinedPredicate, searchPredicate);
            }

            // Filtro por estado activo
            if (requestDto.getActivo() != null) {
                Predicate activoPredicate = cb.equal(root.get("activo"), requestDto.getActivo());
                combinedPredicate = cb.and(combinedPredicate, activoPredicate);
            }
            
            return combinedPredicate;
        };
    }
    
    @Override
    public List<Proveedor> listarPorEstado(Optional<Boolean> activo) {
        Specification<Proveedor> spec = Specification.where(null);
        
        if (activo.isPresent()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("activo"), activo.get()));
        }
        
        return repo.findAll(spec);
    }


	@Override
	public Proveedor obtener(Long id) {
		return repo.findById(id).orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
	}

	@Override
	public Proveedor crear(Proveedor proveedor) {

		if (repo.existsByNitIgnoreCase(proveedor.getNit())) {
			throw new RuntimeException("El NIT ya está registrado");
		}
		
		return repo.save(proveedor);
	}

	@Override
	public Proveedor actualizar(Long id, Proveedor proveedor) {

		Proveedor existente = obtener(id); // Usamos el método 'obtener' local

		if (!existente.getNit().equalsIgnoreCase(proveedor.getNit())
				&& repo.existsByNitIgnoreCase(proveedor.getNit())) {
			throw new RuntimeException("Ya existe otro proveedor con este NIT");
		}

		existente.setNombre(proveedor.getNombre());
		existente.setNit(proveedor.getNit());
		existente.setTelefono(proveedor.getTelefono());
		existente.setEmail(proveedor.getEmail());
		existente.setDireccion(proveedor.getDireccion());
		existente.setActivo(proveedor.getActivo());

		return repo.save(existente);
	}

	@Override
	public void eliminar(Long id) {
		repo.delete(obtener(id));
	}

	@Override
	public boolean existeNit(String nit) {
		return repo.existsByNitIgnoreCase(nit);
	}
}
