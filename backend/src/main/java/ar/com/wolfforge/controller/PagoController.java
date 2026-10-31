package ar.com.wolfforge.controller;

import ar.com.wolfforge.dto.response.MercadoPagoPreferenceResponseDTO;
import ar.com.wolfforge.dto.response.PagoResponseDTO;
import ar.com.wolfforge.service.impl.MercadoPagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para gestionar pagos
 */
@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final MercadoPagoService mercadoPagoService;

    /**
     * Crea una preferencia de pago en Mercado Pago para un pedido
     * Devuelve la URL a la que debe ser redirigido el usuario para pagar
     */
    @PostMapping("/crear-preferencia/{idPedido}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'USUARIO')")
    public ResponseEntity<MercadoPagoPreferenceResponseDTO> crearPreferenciaPago(@PathVariable Long idPedido) {
        MercadoPagoPreferenceResponseDTO preferencia = mercadoPagoService.crearPreferenciaPago(idPedido);
        return ResponseEntity.ok(preferencia);
    }

    /**
     * Versión pública del endpoint de procesar resultado (sin autenticación)
     * Para usar desde la página de retorno de MercadoPago
     */
    @PostMapping("/public/procesar-resultado/{paymentId}")
    public ResponseEntity<String> procesarResultadoPagoPublico(@PathVariable Long paymentId) {
        try {
            // Procesar el webhook manualmente con el payment_id
            mercadoPagoService.procesarWebhookPorPaymentId(paymentId);

            return ResponseEntity.ok("Pago procesado correctamente");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    /**
     * Endpoint para consultar manualmente el estado de un pago en MercadoPago por external_reference
     * Útil para diagnosticar problemas con webhooks
     */
    @GetMapping("/public/consultar-por-pedido/{idPedido}")
    public ResponseEntity<?> consultarPagoPorPedido(@PathVariable Long idPedido) {
        try {
            PagoResponseDTO pagoActualizado = mercadoPagoService.consultarYActualizarPagoPorPedido(idPedido);
            return ResponseEntity.ok(pagoActualizado);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}
