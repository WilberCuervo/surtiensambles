package com.empresarial.auth.domain.exception;


import static com.empresarial.auth.domain.catalog.ErrorCatalog.USUARIO_NO_EXISTE;

public class UsuarioNoEncontradoException extends BusinessException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UsuarioNoEncontradoException(Long id) {

        super(
                USUARIO_NO_EXISTE.code(),
                USUARIO_NO_EXISTE.message(id));

    }

}