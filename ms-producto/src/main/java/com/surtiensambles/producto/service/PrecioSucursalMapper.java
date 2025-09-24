package com.surtiensambles.producto.service;

import org.mapstruct.*;

import com.surtiensambes.commos.producto.dto.PrecioSucursalDTO;
import com.surtiensambes.commos.producto.model.PreciosSucursal;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PrecioSucursalMapper {
	
	PrecioSucursalDTO toDTO(PreciosSucursal entity);

    PreciosSucursal toEntity(PrecioSucursalDTO dto);

    List<PrecioSucursalDTO> toDTOList(List<PreciosSucursal> entities);

    List<PreciosSucursal> toEntityList(List<PrecioSucursalDTO> dtos);
}
