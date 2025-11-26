package com.surtiensambles.inventario.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ErrorDto {
    private String mensaje;
    private String codigoError; 
    private int status;        
    private LocalDateTime fecha;
    private String path; 
}