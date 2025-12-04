package com.surtiensambles.inventario.exception;

// Esta excepción se usará cuando busques un ID y no esté en BD
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String mensaje) {
        super(mensaje);
    }
}