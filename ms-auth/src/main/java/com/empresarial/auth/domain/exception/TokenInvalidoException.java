package com.empresarial.auth.domain.exception;

public class TokenInvalidoException extends BusinessException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TokenInvalidoException() {

        super(
                "AUTH_003","El token enviado no es válido.");

    }

}