package ar.com.wolfforge.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Mercado Pago exige que las "back_urls" (adonde vuelve el usuario después de
 * pagar) sean HTTPS para poder redirigir solo (auto_return). En desarrollo el
 * frontend corre en http://localhost:3000 (sin HTTPS), así que no se puede
 * usar esa URL directamente.
 *
 * Este controlador vive en el backend, que sí tiene una URL pública HTTPS real
 * (vía ngrok). Mercado Pago redirige hasta acá, y desde acá reenviamos al
 * usuario al frontend local, pasando los mismos parámetros de la URL.
 */
@RestController
@Slf4j
public class RedireccionPagoController {

    @Value("${app.frontend-url:http://localhost:3000}")
    private String frontendUrl;

    @GetMapping("/pago/success")
    public void success(HttpServletRequest request, HttpServletResponse response) throws IOException {
        redirigir(request, response, "/pago/success");
    }

    @GetMapping("/pago/failure")
    public void failure(HttpServletRequest request, HttpServletResponse response) throws IOException {
        redirigir(request, response, "/pago/failure");
    }

    @GetMapping("/pago/pending")
    public void pending(HttpServletRequest request, HttpServletResponse response) throws IOException {
        redirigir(request, response, "/pago/pending");
    }

    private void redirigir(HttpServletRequest request, HttpServletResponse response, String path) throws IOException {
        String queryString = request.getQueryString();
        String destino = frontendUrl + path + (queryString != null ? "?" + queryString : "");
        log.info("Redirigiendo vuelta de Mercado Pago hacia el frontend: {}", destino);
        response.sendRedirect(destino);
    }
}
