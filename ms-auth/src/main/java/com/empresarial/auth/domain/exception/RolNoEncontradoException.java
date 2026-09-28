package com.empresarial.auth.domain.exception;

import static com.empresarial.auth.domain.catalog.ErrorCatalog.ROL_NO_EXISTE;

public class RolNoEncontradoException extends BusinessException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RolNoEncontradoException(String rol) {

        super(
                ROL_NO_EXISTE.code(),
                ROL_NO_EXISTE.message(rol));

    }

}