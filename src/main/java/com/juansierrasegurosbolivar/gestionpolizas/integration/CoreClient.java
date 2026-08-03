package com.juansierrasegurosbolivar.gestionpolizas.integration;

import com.juansierrasegurosbolivar.gestionpolizas.dto.request.CoreEventoRequest;

public interface CoreClient {

    void enviarEvento(CoreEventoRequest evento);
}