package com.juansierrasegurosbolivar.gestionpolizas;

import com.juansierrasegurosbolivar.gestionpolizas.entity.Poliza;
import com.juansierrasegurosbolivar.gestionpolizas.entity.Riesgo;
import com.juansierrasegurosbolivar.gestionpolizas.entity.enums.EstadoPoliza;
import com.juansierrasegurosbolivar.gestionpolizas.entity.enums.EstadoRiesgo;
import com.juansierrasegurosbolivar.gestionpolizas.entity.enums.TipoPoliza;
import com.juansierrasegurosbolivar.gestionpolizas.integration.CoreClient;
import com.juansierrasegurosbolivar.gestionpolizas.integration.OperacionCore;
import com.juansierrasegurosbolivar.gestionpolizas.repository.PolizaRepository;
import com.juansierrasegurosbolivar.gestionpolizas.repository.RiesgoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PolizaApiIntegrationTest {

    private static final String API_KEY_HEADER = "x-api-key";
    private static final String API_KEY_VALUE = "123456";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PolizaRepository polizaRepository;

    @Autowired
    private RiesgoRepository riesgoRepository;

    @MockitoBean
    private CoreClient coreClient;

    private Long polizaIndividualActivaId;
    private Long polizaColectivaActivaId;
    private Long polizaCanceladaId;

    @BeforeEach
    void setUp() {
        polizaIndividualActivaId =
            obtenerPoliza(TipoPoliza.INDIVIDUAL, EstadoPoliza.ACTIVA)
                .getId();

        polizaColectivaActivaId =
            obtenerPoliza(TipoPoliza.COLECTIVA, EstadoPoliza.ACTIVA)
                .getId();

        polizaCanceladaId =
            obtenerPoliza(TipoPoliza.INDIVIDUAL, EstadoPoliza.CANCELADA)
                .getId();
    }

    @Test
    void debeRechazarSolicitudSinApiKey() throws Exception {
        mockMvc.perform(
                get("/polizas")
            )
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.status").value(401))
            .andExpect(
                jsonPath("$.message")
                    .value("La API key es inválida o no fue enviada")
            );

        verifyNoInteractions(coreClient);
    }

    @Test
    void debeRechazarSolicitudConApiKeyIncorrecta() throws Exception {
        mockMvc.perform(
                get("/polizas")
                    .header(API_KEY_HEADER, "clave-incorrecta")
            )
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.status").value(401));

        verifyNoInteractions(coreClient);
    }

    @Test
    void debeConsultarTodasLasPolizas() throws Exception {
        mockMvc.perform(
                get("/polizas")
                    .header(API_KEY_HEADER, API_KEY_VALUE)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    void debeFiltrarPolizasPorTipoYEstado() throws Exception {
        mockMvc.perform(
                get("/polizas")
                    .param("tipo", "COLECTIVA")
                    .param("estado", "ACTIVA")
                    .header(API_KEY_HEADER, API_KEY_VALUE)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].tipo").value("COLECTIVA"))
            .andExpect(jsonPath("$[0].estado").value("ACTIVA"));
    }

    @Test
    void debeResponderBadRequestCuandoElTipoEsInvalido()
        throws Exception {

        mockMvc.perform(
                get("/polizas")
                    .param("tipo", "OTRA")
                    .header(API_KEY_HEADER, API_KEY_VALUE)
            )
            .andExpect(status().isBadRequest())
            .andExpect(
                jsonPath("$.message")
                    .value(
                        "El valor 'OTRA' no es válido para el parámetro 'tipo'"
                    )
            );
    }

    @Test
    void debeConsultarLosRiesgosDeUnaPolizaColectiva()
        throws Exception {

        mockMvc.perform(
                get(
                    "/polizas/{id}/riesgos",
                    polizaColectivaActivaId
                )
                    .header(API_KEY_HEADER, API_KEY_VALUE)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].estado").value("ACTIVO"))
            .andExpect(jsonPath("$[1].estado").value("ACTIVO"));
    }

    @Test
    void debeAgregarRiesgoAUnaPolizaColectiva()
        throws Exception {

        String requestBody = """
            {
              "descripcion": "Bodega de almacenamiento",
              "direccionInmueble": "Avenida 68 # 20-50, Bogotá"
            }
            """;

        mockMvc.perform(
                post(
                    "/polizas/{id}/riesgos",
                    polizaColectivaActivaId
                )
                    .header(API_KEY_HEADER, API_KEY_VALUE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
            )
            .andExpect(status().isCreated())
            .andExpect(
                jsonPath("$.polizaId")
                    .value(polizaColectivaActivaId)
            )
            .andExpect(
                jsonPath("$.descripcion")
                    .value("Bodega de almacenamiento")
            )
            .andExpect(jsonPath("$.estado").value("ACTIVO"));

        verify(coreClient).enviarEvento(
            argThat(evento ->
                evento.getOperacion()
                    == OperacionCore.AGREGAR_RIESGO
                && evento.getPolizaId()
                    .equals(polizaColectivaActivaId)
                && evento.getRiesgoId() == null
            )
        );
    }

    @Test
    void noDebeAgregarRiesgoAUnaPolizaIndividual()
        throws Exception {

        String requestBody = """
            {
              "descripcion": "Riesgo adicional",
              "direccionInmueble": "Calle 10 # 20-30, Bogotá"
            }
            """;

        mockMvc.perform(
                post(
                    "/polizas/{id}/riesgos",
                    polizaIndividualActivaId
                )
                    .header(API_KEY_HEADER, API_KEY_VALUE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
            )
            .andExpect(status().isUnprocessableContent())
            .andExpect(
                jsonPath("$.message")
                    .value(
                        "Solo se pueden agregar riesgos a pólizas colectivas"
                    )
            );

        verifyNoInteractions(coreClient);
    }

    @Test
    void debeValidarLosCamposParaCrearUnRiesgo()
        throws Exception {

        String requestBody = """
            {
              "descripcion": "",
              "direccionInmueble": ""
            }
            """;

        mockMvc.perform(
                post(
                    "/polizas/{id}/riesgos",
                    polizaColectivaActivaId
                )
                    .header(API_KEY_HEADER, API_KEY_VALUE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
            )
            .andExpect(status().isBadRequest())
            .andExpect(
                jsonPath("$.detalles.descripcion").exists()
            )
            .andExpect(
                jsonPath("$.detalles.direccionInmueble").exists()
            );

        verifyNoInteractions(coreClient);
    }

    @Test
    void debeRenovarUnaPolizaActiva() throws Exception {
        String requestBody = """
            {
              "ipcPorcentaje": 10
            }
            """;

        mockMvc.perform(
                post(
                    "/polizas/{id}/renovar",
                    polizaIndividualActivaId
                )
                    .header(API_KEY_HEADER, API_KEY_VALUE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.estado").value("RENOVADA"))
            .andExpect(
                jsonPath("$.canonMensual").value(1650000.00)
            )
            .andExpect(
                jsonPath("$.prima").value(19800000.00)
            )
            .andExpect(
                jsonPath("$.fechaInicio").value("2027-01-01")
            )
            .andExpect(
                jsonPath("$.fechaFin").value("2027-12-31")
            );

        verify(coreClient).enviarEvento(
            argThat(evento ->
                evento.getOperacion()
                    == OperacionCore.RENOVAR_POLIZA
                && evento.getPolizaId()
                    .equals(polizaIndividualActivaId)
                && evento.getRiesgoId() == null
            )
        );
    }

    @Test
    void noDebeRenovarUnaPolizaCancelada() throws Exception {
        String requestBody = """
            {
              "ipcPorcentaje": 10
            }
            """;

        mockMvc.perform(
                post(
                    "/polizas/{id}/renovar",
                    polizaCanceladaId
                )
                    .header(API_KEY_HEADER, API_KEY_VALUE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
            )
            .andExpect(status().isUnprocessableContent())
            .andExpect(
                jsonPath("$.message")
                    .value(
                        "No se puede renovar una póliza cancelada"
                    )
            );

        verifyNoInteractions(coreClient);
    }

    @Test
    void debeCancelarUnaPolizaYTodosSusRiesgos()
        throws Exception {

        mockMvc.perform(
                post(
                    "/polizas/{id}/cancelar",
                    polizaColectivaActivaId
                )
                    .header(API_KEY_HEADER, API_KEY_VALUE)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.estado").value("CANCELADA"))
            .andExpect(jsonPath("$.fechaCancelacion").isNotEmpty());

        List<Riesgo> riesgos =
            riesgoRepository.findByPoliza_IdOrderByIdAsc(
                polizaColectivaActivaId
            );

        boolean todosCancelados = riesgos
            .stream()
            .allMatch(riesgo ->
                riesgo.getEstado() == EstadoRiesgo.CANCELADO
                    && riesgo.getFechaCancelacion() != null
            );

        org.junit.jupiter.api.Assertions.assertTrue(
            todosCancelados,
            "Todos los riesgos deben quedar cancelados"
        );

        verify(coreClient).enviarEvento(
            argThat(evento ->
                evento.getOperacion()
                    == OperacionCore.CANCELAR_POLIZA
                && evento.getPolizaId()
                    .equals(polizaColectivaActivaId)
            )
        );
    }

    @Test
    void debeCancelarUnRiesgoActivo() throws Exception {
        Riesgo riesgoActivo = riesgoRepository
            .findByPoliza_IdAndEstadoOrderByIdAsc(
                polizaColectivaActivaId,
                EstadoRiesgo.ACTIVO
            )
            .get(0);

        mockMvc.perform(
                post(
                    "/riesgos/{id}/cancelar",
                    riesgoActivo.getId()
                )
                    .header(API_KEY_HEADER, API_KEY_VALUE)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.estado").value("CANCELADO"))
            .andExpect(jsonPath("$.fechaCancelacion").isNotEmpty());

        verify(coreClient).enviarEvento(
            argThat(evento ->
                evento.getOperacion()
                    == OperacionCore.CANCELAR_RIESGO
                && evento.getPolizaId()
                    .equals(polizaColectivaActivaId)
                && evento.getRiesgoId()
                    .equals(riesgoActivo.getId())
            )
        );
    }

    @Test
    void debeResponderNotFoundCuandoLaPolizaNoExiste()
        throws Exception {

        mockMvc.perform(
                get("/polizas/{id}/riesgos", 999999L)
                    .header(API_KEY_HEADER, API_KEY_VALUE)
            )
            .andExpect(status().isNotFound())
            .andExpect(
                jsonPath("$.message")
                    .value("No se encontró la póliza con id 999999")
            );
    }

    @Test
    void debeAceptarUnaSolicitudDirectaAlCoreMock()
        throws Exception {

        String requestBody = """
            {
              "operacion": "RENOVAR_POLIZA",
              "polizaId": 1,
              "riesgoId": null
            }
            """;

        mockMvc.perform(
                post("/core-mock/evento")
                    .header(API_KEY_HEADER, API_KEY_VALUE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
            )
            .andExpect(status().isNoContent());
    }

    private Poliza obtenerPoliza(
        TipoPoliza tipo,
        EstadoPoliza estado
    ) {
        return polizaRepository
            .findByTipoAndEstadoOrderByIdAsc(tipo, estado)
            .stream()
            .findFirst()
            .orElseThrow(() ->
                new IllegalStateException(
                    "No se encontró una póliza inicial de tipo "
                        + tipo
                        + " y estado "
                        + estado
                )
            );
    }
}