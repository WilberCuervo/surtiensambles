package com.empresarial.auth.domain.catalog;

public class ErrorMessage {

    private final String code;

    private final String message;

    public ErrorMessage(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String code() {
        return code;
    }

    public String message(Object... args) {

        String formatted = message;

        for (Object arg : args) {
            formatted = formatted.replaceFirst("%s", String.valueOf(arg));
        }

        return formatted;
    }

}