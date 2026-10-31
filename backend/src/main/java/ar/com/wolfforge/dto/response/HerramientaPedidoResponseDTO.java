package ar.com.wolfforge.dto.response;

import ar.com.wolfforge.enums.EstadoPedido;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HerramientaPedidoResponseDTO { //Usado cuando la API devuelve datos.

    private Long idHerramientaPedido;
    private int cantidad;
    private EstadoPedido estadoPedido;
    private boolean solicitudDevolucion;

    //Informacion relacionada
    private Long idHerramienta;
    private String nombreHerramienta;
    private BigDecimal precioTotal;

    private Long idUsuario;
    private String nombreUsuario;
}
