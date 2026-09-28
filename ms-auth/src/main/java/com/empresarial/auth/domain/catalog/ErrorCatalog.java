package com.empresarial.auth.domain.catalog;

public final class ErrorCatalog {

    private ErrorCatalog() {
    }

    /*
     * ==========================
     * USUARIOS
     * ==========================
     */

    public static final ErrorMessage USUARIO_YA_EXISTE =
            new ErrorMessage("USR_001","El usuario '%s' ya existe.");

    public static final ErrorMessage USUARIO_NO_EXISTE =
            new ErrorMessage("USR_002","No existe un usuario con id %s.");

    /*
     * ==========================
     * ROLES
     * ==========================
     */

    public static final ErrorMessage ROL_NO_EXISTE =
            new ErrorMessage("ROL_001","No existe el rol '%s'.");

    /*
     * ==========================
     * AUTH
     * ==========================
     */

    public static final ErrorMessage CREDENCIALES_INVALIDAS =
            new ErrorMessage("AUTH_001","Usuario o contraseña incorrectos.");

    public static final ErrorMessage PASSWORD_INCORRECTO =
            new ErrorMessage("AUTH_002","La contraseña actual es incorrecta.");

    public static final ErrorMessage TOKEN_INVALIDO =
            new ErrorMessage("AUTH_003","El token enviado no es válido.");

}
