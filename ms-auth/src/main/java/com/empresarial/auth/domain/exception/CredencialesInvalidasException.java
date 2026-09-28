package com.empresarial.auth.domain.exception;

public class CredencialesInvalidasException extends BusinessException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CredencialesInvalidasException() {

        super(
                "AUTH_001","Usuario o contraseña incorrectos");

    }

}