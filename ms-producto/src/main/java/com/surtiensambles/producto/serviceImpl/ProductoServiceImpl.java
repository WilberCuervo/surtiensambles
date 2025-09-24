package com.surtiensambles.producto.serviceImpl;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.surtiensambes.commos.producto.dto.ProductoDTO;
import com.surtiensambles.producto.dao.ProductoRepository;
import com.surtiensambles.producto.entity.Producto;

import com.surtiensambles.producto.service.ProductoService;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {
	
	private ProductoRepository productoRepository;
	
    private final ModelMapper modelMapper;

	@Override
	public ProductoDTO  createProducto(ProductoDTO  productoDto) {
        // Convertir DTO a entidad
        Producto entity = modelMapper.map(productoDto, Producto.class);

        // Guardar en BD
        entity = productoRepository.save(entity);

        // Convertir de nuevo a DTO y devolver
        return modelMapper.map(entity, ProductoDTO.class);

	}

	 @Override
	    public Optional<Producto> getProducto(Long id) {
	        return productoRepository.findById(id);
	                //.map(entity -> modelMapper.map(entity, Producto.class));
	    }

	@Override
	public void juan() {
		System.out.println(" hola juan");
		
	}
}
