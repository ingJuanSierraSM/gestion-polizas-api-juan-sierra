package com.juansierrasegurosbolivar.gestionpolizas.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApiKeyFilter extends OncePerRequestFilter {

    private static final String API_KEY_HEADER = "x-api-key";

    private final String expectedApiKey;

    public ApiKeyFilter(
        @Value("${app.security.api-key:123456}") String expectedApiKey
    ) {
        this.expectedApiKey = expectedApiKey;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        String providedApiKey =
            request.getHeader(API_KEY_HEADER);

        if (!isValidApiKey(providedApiKey)) {
            writeUnauthorizedResponse(request, response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(
        HttpServletRequest request
    ) {
        String path = request.getRequestURI();

        return path.startsWith("/h2-console")
            || path.equals("/error");
    }

    private boolean isValidApiKey(String providedApiKey) {
        if (providedApiKey == null) {
            return false;
        }

        byte[] expectedBytes =
            expectedApiKey.getBytes(StandardCharsets.UTF_8);

        byte[] providedBytes =
            providedApiKey.getBytes(StandardCharsets.UTF_8);

        return MessageDigest.isEqual(
            expectedBytes,
            providedBytes
        );
    }

    private void writeUnauthorizedResponse(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws IOException {

        response.setStatus(
            HttpStatus.UNAUTHORIZED.value()
        );

        response.setContentType(
            MediaType.APPLICATION_JSON_VALUE
        );

        response.setCharacterEncoding(
            StandardCharsets.UTF_8.name()
        );

        String body = """
            {
              "status": 401,
              "error": "Unauthorized",
              "message": "La API key es inválida o no fue enviada",
              "path": "%s"
            }
            """.formatted(request.getRequestURI());

        response.getWriter().write(body);
    }
}