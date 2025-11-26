package com.surtiensambles.inventario.exception;

import com.surtiensambles.inventario.dto.ErrorDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 1. Manejo de Recurso No Encontrado (404)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDto> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        ErrorDto error = ErrorDto.builder()
                .mensaje(ex.getMessage())
                .codigoError("RECURSO_NO_ENCONTRADO")
                .status(HttpStatus.NOT_FOUND.value())
                .fecha(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // 2. Manejo de Reglas de Negocio (400) - Ej: Stock insuficiente
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorDto> handleBusinessException(BusinessException ex, HttpServletRequest request) {
        ErrorDto error = ErrorDto.builder()
                .mensaje(ex.getMessage())
                .codigoError("REGLA_NEGOCIO_VIOLADA")
                .status(HttpStatus.BAD_REQUEST.value())
                .fecha(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // 3. Manejo de Integridad de Datos (Conflictos de BD) - Ej: Borrar producto con stock
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDto> handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest request) {
        String mensaje = "No se puede procesar la operación por restricciones de datos (ej: registros relacionados).";
        
        ErrorDto error = ErrorDto.builder()
                .mensaje(mensaje)
                .codigoError("CONFLICTO_DATOS")
                .status(HttpStatus.CONFLICT.value())
                .fecha(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    // 4. Manejo de Validaciones (@Valid en DTOs)
    // Este es especial: devuelve un mapa con campo -> error
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> errores = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errores.put(error.getField(), error.getDefaultMessage());
        });

        response.put("mensaje", "Hay errores en los datos enviados");
        response.put("errores", errores);
        response.put("fecha", LocalDateTime.now());
        
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 5. Manejo General (Cualquier cosa que se nos pasó) - 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleGeneralException(Exception ex, HttpServletRequest request) {
        ex.printStackTrace();
        
        ErrorDto error = ErrorDto.builder()
                .mensaje("Ocurrió un error interno inesperado. Contacte al administrador.")
                .codigoError("ERROR_INTERNO")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .fecha(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}