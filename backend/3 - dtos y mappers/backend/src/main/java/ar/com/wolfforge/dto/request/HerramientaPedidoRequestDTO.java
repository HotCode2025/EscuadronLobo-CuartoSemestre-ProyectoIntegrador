package ar.com.wolfforge.dto.request;

import ar.com.wolfforge.enums.EstadoPedido;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HerramientaPedidoRequestDTO {

    @NotNull(message = "Debe indicar el ID de la herramienta")
    private Long idHerramienta;

    @NotNull(message = "Debe indicar el ID del usuario")
    private Long idUsuario;

    @Min(value = 1, message = "La cantidad minima es 1")
    private int cantidad;

    private EstadoPedido estadoPedido;
}
