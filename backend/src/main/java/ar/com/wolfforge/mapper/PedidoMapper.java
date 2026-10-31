package ar.com.wolfforge.mapper;

import ar.com.wolfforge.dto.response.HerramientaPedidoResponseDTO;
import ar.com.wolfforge.dto.response.PedidoResponseDTO;
import ar.com.wolfforge.dto.response.UsuarioResponseDTO;
import ar.com.wolfforge.entity.HerramientaPedido;
import ar.com.wolfforge.entity.Pedido;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PedidoMapper {

    // Convierte Pedido entity a PedidoResponseDTO
    public PedidoResponseDTO toResponseDTO(Pedido pedido) {
        if (pedido == null) return null;

        PedidoResponseDTO dto = new PedidoResponseDTO();
        dto.setIdPedido(pedido.getIdPedido());
        dto.setPrecioTotal(pedido.getPrecioTotal());
        dto.setCreadoEn(pedido.getCreadoEn());

        // Mapear usuario (información básica)
        if (pedido.getUsuario() != null) {
            UsuarioResponseDTO usuarioDTO = new UsuarioResponseDTO();
            usuarioDTO.setIdUsuario(pedido.getUsuario().getIdUsuario());
            usuarioDTO.setNombre(pedido.getUsuario().getNombre());
            usuarioDTO.setEmail(pedido.getUsuario().getEmail());
            usuarioDTO.setNumeroTelefonico(pedido.getUsuario().getNumeroTelefono());
            dto.setUsuario(usuarioDTO);
        }

        // Mapear lista de herramientas del pedido
        if (pedido.getListaHerramientas() != null) {
            List<HerramientaPedidoResponseDTO> herramientasDTO = pedido.getListaHerramientas()
                    .stream()
                    .map(this::toHerramientaPedidoResponseDTO)
                    .collect(Collectors.toList());
            dto.setHerramientas(herramientasDTO);
        }

        return dto;
    }

    // Convierte HerramientaPedido entity a HerramientaPedidoResponseDTO
    public HerramientaPedidoResponseDTO toHerramientaPedidoResponseDTO(HerramientaPedido herramientaPedido) {
        if (herramientaPedido == null) return null;

        HerramientaPedidoResponseDTO dto = new HerramientaPedidoResponseDTO();
        dto.setIdHerramientaPedido(herramientaPedido.getIdHerramientaPedido());
        dto.setCantidad(herramientaPedido.getCantidad());
        dto.setEstadoPedido(herramientaPedido.getEstadoPedido());
        dto.setSolicitudDevolucion(herramientaPedido.isSolicitudDevolucion());

        // Información de la herramienta
        if (herramientaPedido.getHerramienta() != null) {
            dto.setIdHerramienta(herramientaPedido.getHerramienta().getIdHerramienta());
            dto.setNombreHerramienta(herramientaPedido.getHerramienta().getNombre());
            // Calcular precio total (precio unitario * cantidad)
            dto.setPrecioTotal(herramientaPedido.getHerramienta().getPrecio()
                    .multiply(java.math.BigDecimal.valueOf(herramientaPedido.getCantidad())));
        }

        // Información del usuario
        if (herramientaPedido.getUsuario() != null) {
            dto.setIdUsuario(herramientaPedido.getUsuario().getIdUsuario());
            dto.setNombreUsuario(herramientaPedido.getUsuario().getNombre());
        }

        return dto;
    }
}
