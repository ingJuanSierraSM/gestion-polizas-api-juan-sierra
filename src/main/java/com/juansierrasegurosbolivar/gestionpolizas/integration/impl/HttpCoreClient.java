package com.juansierrasegurosbolivar.gestionpolizas.integration.impl;

import com.juansierrasegurosbolivar.gestionpolizas.dto.request.CoreEventoRequest;
import com.juansierrasegurosbolivar.gestionpolizas.exception.CoreIntegrationException;
import com.juansierrasegurosbolivar.gestionpolizas.integration.CoreClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Slf4j
@Component
public class HttpCoreClient implements CoreClient {

    private final RestClient restClient;

    public HttpCoreClient(
        RestClient.Builder restClientBuilder,
        @Value("${app.core.base-url}") String baseUrl,
        @Value("${app.security.api-key}") String apiKey
    ) {
        this.restClient = restClientBuilder
            .baseUrl(baseUrl)
            .defaultHeader("x-api-key", apiKey)
            .build();
    }

    @Override
    public void enviarEvento(CoreEventoRequest evento) {
        try {
            log.info(
                "Enviando operación {} al CORE. polizaId={}, riesgoId={}",
                evento.getOperacion(),
                evento.getPolizaId(),
                evento.getRiesgoId()
            );

            restClient
                .post()
                .uri("/core-mock/evento")
                .contentType(MediaType.APPLICATION_JSON)
                .body(evento)
                .retrieve()
                .toBodilessEntity();

            log.info(
                "Operación {} confirmada por el CORE. polizaId={}",
                evento.getOperacion(),
                evento.getPolizaId()
            );

        } catch (RestClientException exception) {
            log.error(
                "Falló la operación {} en el CORE. polizaId={}",
                evento.getOperacion(),
                evento.getPolizaId(),
                exception
            );

            throw new CoreIntegrationException(
                "No fue posible completar la operación en el CORE",
                exception
            );
        }
    }
}