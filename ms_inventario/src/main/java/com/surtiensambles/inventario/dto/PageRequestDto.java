package com.surtiensambles.inventario.dto;

import lombok.Data;
import org.springframework.data.domain.Sort;
import java.time.LocalDate;

@Data
public class PageRequestDto {
    private int page = 0;
    private int size = 10;
    private String search;
    
    private Long categoriaId;
    private Boolean activo;
    
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    // ---------------------------------

    private String sortBy = "id";
    private Sort.Direction sortDirection = Sort.Direction.ASC;
}