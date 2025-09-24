package com.surtiensambles.producto.service;

import org.mapstruct.*;

import com.surtiensambes.commos.producto.dto.PresentacionDTO;
import com.surtiensambes.commos.producto.model.Presentacion;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PresentacionMapper {
	
	PresentacionDTO toDTO(Presentacion entity);

    Presentacion toEntity(PresentacionDTO dto);

    List<PresentacionDTO> toDTOList(List<Presentacion> entities);

    List<Presentacion> toEntityList(List<PresentacionDTO> dtos);

}
