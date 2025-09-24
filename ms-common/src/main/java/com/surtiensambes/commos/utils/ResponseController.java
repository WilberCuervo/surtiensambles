package com.surtiensambes.commos.utils;

import org.springframework.http.HttpStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta genérica estándar de la API")
public class ResponseController<T> {

    @Schema(description = "Indica si la operación fue exitosa", example = "true")
    private boolean success;

    @Schema(description = "Mensaje descriptivo de la respuesta", example = "Operación realizada con éxito")
    private String message;

    @Schema(description = "Datos devueltos por la operación")
    private T data;

    @Schema(description = "Código de estado HTTP", example = "200")
    private int status;

    public static <T> ResponseController<T> success(String message, T data) {
        return new ResponseController<>(true, message, data, HttpStatus.OK.value());
    }

    public static <T> ResponseController<T> success(String message, T data, HttpStatus status) {
        return new ResponseController<>(true, message, data, status.value());
    }

    public static <T> ResponseController<T> error(String message) {
        return new ResponseController<>(false, message, null, HttpStatus.BAD_REQUEST.value());
    }

    public static <T> ResponseController<T> error(String message, HttpStatus status) {
        return new ResponseController<>(false, message, null, status.value());
    }
}
