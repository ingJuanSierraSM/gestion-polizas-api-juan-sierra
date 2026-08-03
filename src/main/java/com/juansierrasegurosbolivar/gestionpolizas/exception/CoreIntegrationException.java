package com.juansierrasegurosbolivar.gestionpolizas.exception;

public class CoreIntegrationException extends RuntimeException {

    public CoreIntegrationException(
        String message,
        Throwable cause
    ) {
        super(message, cause);
    }
}