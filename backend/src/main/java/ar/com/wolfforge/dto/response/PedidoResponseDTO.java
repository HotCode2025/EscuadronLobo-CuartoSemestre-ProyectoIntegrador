package ar.com.wolfforge.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoResponseDTO {

    private Long idPedido;
    private BigDecimal precioTotal;
    private LocalDateTime creadoEn;

    private UsuarioResponseDTO usuario;
    private List<HerramientaPedidoResponseDTO> herramientas; // lista de herramientas del pedido
}
