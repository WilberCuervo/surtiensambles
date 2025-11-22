package com.surtiensambles.inventario.serviceImpl;

import com.surtiensambles.inventario.entity.Proveedor;
import com.surtiensambles.inventario.repository.ProveedorRepository;
import com.surtiensambles.inventario.service.ProveedorService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProveedorServiceImpl implements ProveedorService {

	private final ProveedorRepository repo;

	@Override
	public Page<Proveedor> list(int page, int size, String search, Boolean activo, String sort) {

		String[] sortParts = sort.split(",");
		String sortByField = sortParts[0];
		Sort.Direction sortDirection = (sortParts.length == 2 && sortParts[1].equalsIgnoreCase("desc"))
				? Sort.Direction.DESC
				: Sort.Direction.ASC;

		Sort order = Sort.by(sortDirection, sortByField);

		Pageable pageable = PageRequest.of(page, size, order);

		Specification<Proveedor> spec = Specification.where(null);

		if (search != null && !search.trim().isEmpty()) {
			String like = "%" + search.toLowerCase() + "%";
			Specification<Proveedor> searchSpec = (root, query, cb) -> cb
					.or(cb.like(cb.lower(root.get("nombre")), like), cb.like(cb.lower(root.get("nit")), like));
			spec = spec.and(searchSpec);
		}

		if (activo != null) {
			Specification<Proveedor> activoSpec = (root, query, cb) -> cb.equal(root.get("activo"), activo);
			spec = spec.and(activoSpec);
		}

		return repo.findAll(spec, pageable);
	}

	@Override
	public Proveedor get(Long id) {
		return repo.findById(id).orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
	}

	@Override
	public Proveedor create(Proveedor proveedor) {

		if (repo.existsByNitIgnoreCase(proveedor.getNit())) {
			throw new RuntimeException("El NIT ya está registrado");
		}

		return repo.save(proveedor);
	}

	@Override
	public Proveedor update(Long id, Proveedor proveedor) {

		Proveedor existente = get(id);

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
	public void delete(Long id) {
		repo.delete(get(id));
	}

	@Override
	public boolean existeNit(String nit) {
		return repo.existsByNitIgnoreCase(nit);
	}
}
