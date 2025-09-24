package com.surtiensambles.producto.controller;

import com.surtiensambes.commos.utils.ResponseController;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler<T> {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseController<T>> handleValidationErrors(MethodArgumentNotValidException ex) {
    	
        String mensaje = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Datos inválidos");
        
        ResponseController <T> response = ResponseController.<T>builder()
	        .success(false)
	        .message(mensaje)
	        .status(HttpStatus.BAD_REQUEST.value())
	        .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseController<T>> handleConstraintViolation(ConstraintViolationException ex) {
        
    	ResponseController<T> response = ResponseController.<T>builder()
	        .success(false)
	        .message(ex.getMessage())
	        .status(HttpStatus.BAD_REQUEST.value())
	        .build();
    	
    	return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseController<T>> handleGenericException(Exception ex) {
        
    	ResponseController<T> response = ResponseController.<T>builder()
												          .success(false)
												          .message("Ocurrió un error inesperado: " + ex.getMessage())
												          .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
												          .build();
    	
    	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
