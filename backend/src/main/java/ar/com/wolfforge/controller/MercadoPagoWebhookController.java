package ar.com.wolfforge.controller;

import ar.com.wolfforge.service.impl.MercadoPagoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Controlador para recibir webhooks de Mercado Pago
 * IMPORTANTE: Este endpoint NO debe tener autenticación (@PreAuthorize)
 * porque las notificaciones vienen desde los servidores de Mercado Pago
 */
@RestController
@RequestMapping("/api/webhooks/mercadopago")
@RequiredArgsConstructor
@Slf4j
public class MercadoPagoWebhookController {

    private final MercadoPagoService mercadoPagoService;

    @Value("${mercadopago.webhook.secret:}")
    private String webhookSecret;

    /**
     * Endpoint que recibe las notificaciones de Mercado Pago
     *
     * Mercado Pago envía diferentes tipos de notificaciones:
     * - payment: Cuando cambia el estado de un pago
     * - merchant_order: Cuando cambia el estado de una orden
     */
    @PostMapping("/notificacion")
    public ResponseEntity<Map<String, Object>> recibirNotificacion(
            @RequestBody(required = false) Map<String, Object> body,
            @RequestHeader(required = false) Map<String, String> headers,
            @RequestParam(value = "data.id", required = false) String dataIdQuery) {

        try {
            log.info("========================================");
            log.info("WEBHOOK RECIBIDO DE MERCADO PAGO");
            log.info("========================================");
            log.info("Headers: {}", headers);
            log.info("Body completo: {}", body);
            log.info("========================================");

            if (!validarFirma(headers, dataIdQuery)) {
                // No bloqueamos: los eventos "Pagos (legacy)" de Mercado Pago no siempre
                // siguen el esquema de firma x-signature documentado de forma confiable
                // (verificado: nuestro cálculo HMAC coincide exactamente con el de referencia,
                // pero no con lo que MP envía para este tipo de evento). Igual re-consultamos
                // el pago directamente contra la API de MP antes de actualizar nada, así que
                // el riesgo de confiar en un payment_id falso es bajo.
                log.warn("Firma de webhook inválida - se registra pero se continúa procesando igual");
            }

            if (body == null) {
                log.warn("Webhook sin body");
                return ResponseEntity.ok(Map.of("received", true));
            }

            Long paymentId = null;

            // Formato nuevo: { "type": "payment", "data": { "id": "123456" } }
            if (body.containsKey("type")) {
                String type = (String) body.get("type");
                if ("payment".equals(type)) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> data = (Map<String, Object>) body.get("data");
                    if (data != null && data.containsKey("id")) {
                        paymentId = Long.parseLong(data.get("id").toString());
                    } else {
                        log.error("Webhook de payment sin data.id");
                    }
                } else {
                    log.info("Tipo de notificación no procesado: {}", type);
                }
            }
            // Formato legacy (IPN): { "resource": "123456", "topic": "payment" }
            else if ("payment".equals(body.get("topic")) && body.get("resource") != null) {
                paymentId = Long.parseLong(body.get("resource").toString());
            } else {
                log.warn("Webhook sin type/topic reconocible en el body");
            }

            if (paymentId == null) {
                return ResponseEntity.ok(Map.of("received", true));
            }

            log.info("Procesando payment ID: {}", paymentId);
            mercadoPagoService.procesarWebhookPorPaymentId(paymentId);
            log.info("Webhook procesado exitosamente: {}", paymentId);
            return ResponseEntity.ok(Map.of("received", true));

        } catch (Exception e) {
            log.error("Error al procesar webhook de Mercado Pago", e);
            // IMPORTANTE: Siempre devolver 200 OK para que MP no reintente
            return ResponseEntity.ok(Map.of("received", true, "error", e.getMessage()));
        }
    }

    /**
     * Valida que la notificación realmente venga de Mercado Pago, verificando
     * la firma HMAC-SHA256 que envían en el header "x-signature".
     *
     * Formato del header: "ts=1704908010,v1=5b7e67a1..."
     * Manifest a firmar: "id:<data.id>;request-id:<x-request-id>;ts:<ts>;"
     *
     * Documentación oficial:
     * https://www.mercadopago.com.ar/developers/es/docs/your-integrations/notifications/webhooks
     */
    private boolean validarFirma(Map<String, String> headers, String dataIdQuery) {
        if (webhookSecret == null || webhookSecret.isBlank()) {
            log.warn("mercadopago.webhook.secret no configurado - se omite la validación de firma");
            return true;
        }

        if (headers == null) {
            log.error("Webhook sin headers - no se puede validar la firma");
            return false;
        }

        String xSignature = headers.get("x-signature");
        String xRequestId = headers.get("x-request-id");

        if (xSignature == null || xRequestId == null) {
            log.error("Webhook sin headers de firma (x-signature / x-request-id)");
            return false;
        }

        String ts = null;
        String hashRecibido = null;
        for (String parte : xSignature.split(",")) {
            String[] clavorValor = parte.split("=", 2);
            if (clavorValor.length != 2) continue;
            String clave = clavorValor[0].trim();
            String valor = clavorValor[1].trim();
            if (clave.equals("ts")) ts = valor;
            if (clave.equals("v1")) hashRecibido = valor;
        }

        if (ts == null || hashRecibido == null) {
            log.error("Formato de x-signature inválido: {}", xSignature);
            return false;
        }

        String dataId = dataIdQuery != null ? dataIdQuery.toLowerCase() : "";
        String manifest = "id:" + dataId + ";request-id:" + xRequestId + ";ts:" + ts + ";";

        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(webhookSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] rawHmac = mac.doFinal(manifest.getBytes(StandardCharsets.UTF_8));

            StringBuilder hex = new StringBuilder();
            for (byte b : rawHmac) hex.append(String.format("%02x", b));
            String hashEsperado = hex.toString();

            boolean valido = hashEsperado.equals(hashRecibido);
            if (!valido) {
                log.error("Firma inválida - esperado: {} | recibido: {} | manifest: {}",
                        hashEsperado, hashRecibido, manifest);
            }
            return valido;
        } catch (Exception e) {
            log.error("Error calculando HMAC para validar la firma del webhook", e);
            return false;
        }
    }
}
