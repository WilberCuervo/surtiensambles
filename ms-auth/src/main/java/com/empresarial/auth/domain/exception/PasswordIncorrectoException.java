package com.empresarial.auth.domain.exception;

public class PasswordIncorrectoException extends BusinessException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PasswordIncorrectoException() {

        super(
                "AUTH_002","La contraseña actual es incorrecta.");

    }

}