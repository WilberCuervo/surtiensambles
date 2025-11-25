package com.surtiensambles.inventario.serviceImpl;

import com.surtiensambles.inventario.dto.PageRequestDto;
import com.surtiensambles.inventario.entity.Bodega;
import com.surtiensambles.inventario.repository.BodegaRepository;
import com.surtiensambles.inventario.service.BodegaService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.criteria.Predicate;

@Service
@RequiredArgsConstructor
public class BodegaServiceImpl implements BodegaService {

	private final BodegaRepository repo;

    /**
     * Implementación centralizada de listado paginado y filtrado para Bodegas.
     */
	@Override
	public Page<Bodega> listarPaginado(PageRequestDto requestDto) {

		// 1. Convertir DTO a objetos Pageable y Sort de Spring
		Sort order = Sort.by(requestDto.getSortDirection(), requestDto.getSortBy());
		Pageable pageable = PageRequest.of(requestDto.getPage(), requestDto.getSize(), order);

		// 2. Construir la Specification dinámica
		Specification<Bodega> spec = buildSpecification(requestDto);

		// 3. Ejecutar la consulta
		return repo.findAll(spec, pageable);
	}

    /**
     * Método helper privado para construir la Specification dinámica de Bodega.
     * Maneja la búsqueda por nombre/codigo y por estado activo.
     */
    private Specification<Bodega> buildSpecification(PageRequestDto requestDto) {
        return (root, query, cb) -> {
            Predicate combinedPredicate = cb.conjunction();

            // Filtro por nombre o Codigo (búsqueda general)
            if (requestDto.getSearch() != null && !requestDto.getSearch().trim().isEmpty()) {
                String searchLowerCase = "%" + requestDto.getSearch().toLowerCase() + "%";
                Predicate nombrePredicate = cb.like(cb.lower(root.get("nombre")), searchLowerCase);
                Predicate codigoPredicate = cb.like(cb.lower(root.get("codigo")), searchLowerCase);
                
                // Combina OR (nombre OR codigo)
                Predicate searchPredicate = cb.or(nombrePredicate, codigoPredicate);
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
    public List<Bodega> listarPorEstado(Optional<Boolean> activo) {
        Specification<Bodega> spec = Specification.where(null);
        
        if (activo.isPresent()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("activo"), activo.get()));
        }
        
        return repo.findAll(spec);
    }

	@Override
	public Bodega obtener(Long id) {
		return repo.findById(id).orElseThrow(() -> new RuntimeException("Bodega no encontrada"));
	}

	@Override
	public Bodega crear(Bodega bodega) {
		if (repo.existsByCodigo(bodega.getCodigo())) {
			throw new RuntimeException("El código de bodega ya está registrado");
		}

		return repo.save(bodega);
	}

	@Override
	public Bodega actualizar(Long id, Bodega bodega) {

		Bodega existente = obtener(id);

		if (!existente.getCodigo().equalsIgnoreCase(bodega.getCodigo())
				&& repo.existsByCodigo(bodega.getCodigo())) {
			throw new RuntimeException("Ya existe otra bodega con este código");
		}

		existente.setNombre(bodega.getNombre());
		existente.setCodigo(bodega.getCodigo());
		existente.setUbicacion(bodega.getUbicacion());
		existente.setResponsable(bodega.getResponsable());
		existente.setActivo(bodega.getActivo());

		return repo.save(existente);
	}

	@Override
	public void eliminar(Long id) {
        Bodega bodega = obtener(id);
		repo.delete(bodega);
	}

	@Override
	public boolean existeCodigo(String codigo) {
		return repo.existsByCodigo(codigo);
	}
}
