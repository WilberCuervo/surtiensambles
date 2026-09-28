package com.empresarial.auth.infrastructure.entrypoints.reactiveweb.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;

import com.empresarial.auth.domain.exception.BusinessException;
import com.empresarial.auth.domain.exception.RolNoEncontradoException;
import com.empresarial.auth.domain.exception.UsuarioNoEncontradoException;
import com.empresarial.auth.domain.exception.UsuarioYaExisteException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UsuarioYaExisteException.class)
	public ResponseEntity<ApiError> handleUsuarioExiste(UsuarioYaExisteException ex, ServerWebExchange exchange) {

		return buildError(HttpStatus.CONFLICT, ex, exchange);

	}

	@ExceptionHandler({ UsuarioNoEncontradoException.class, RolNoEncontradoException.class })
	public ResponseEntity<ApiError> handleNotFound(BusinessException ex, ServerWebExchange exchange) {

		return buildError(HttpStatus.NOT_FOUND, ex, exchange);

	}

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ApiError> handleBusiness(BusinessException ex, ServerWebExchange exchange) {

		return buildError(HttpStatus.BAD_REQUEST, ex, exchange);

	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> handleException(Exception ex, ServerWebExchange exchange) {

		ApiError error = ApiError.builder().timestamp(LocalDateTime.now())
				.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).code("GEN_001").message(ex.getMessage())
				.path(exchange.getRequest().getPath().value()).build();

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);

	}

	private ResponseEntity<ApiError> buildError(HttpStatus status, BusinessException ex, ServerWebExchange exchange) {

		ApiError error = ApiError.builder().timestamp(LocalDateTime.now()).status(status.value())
				.error(status.getReasonPhrase()).code(ex.getCode()).message(ex.getMessage())
				.path(exchange.getRequest().getPath().value()).build();

		return ResponseEntity.status(status).body(error);

	}

}