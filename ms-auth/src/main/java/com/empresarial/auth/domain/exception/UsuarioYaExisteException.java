package com.empresarial.auth.domain.exception;

import static com.empresarial.auth.domain.catalog.ErrorCatalog.USUARIO_YA_EXISTE;

public class UsuarioYaExisteException extends BusinessException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UsuarioYaExisteException(String username) {
		super(
                USUARIO_YA_EXISTE.code(),
                USUARIO_YA_EXISTE.message(username));
    }

}
