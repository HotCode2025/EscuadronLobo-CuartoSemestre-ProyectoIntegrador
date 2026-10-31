package ar.com.wolfforge.dto.response;

import ar.com.wolfforge.enums.EstadoPago;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagoResponseDTO {

    private Long idPago;
    private BigDecimal monto;
    private String metodoPago;
    private EstadoPago estado;

    // Campos específicos de Mercado Pago
    private Long paymentId;
    private String preferenceId;
    private String externalReference;
    private String paymentType;
    private Integer installments;
    private String payerEmail;

    // Datos del pedido asociado
    private Long idPedido;
    private BigDecimal precioTotalPedido;

    private LocalDateTime creadoEn;
    private LocalDateTime actualizadoEn;
}
