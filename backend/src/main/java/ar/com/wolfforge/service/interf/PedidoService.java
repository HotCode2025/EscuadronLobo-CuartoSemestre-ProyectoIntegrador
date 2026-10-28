package ar.com.wolfforge.service.interf;

import ar.com.wolfforge.dto.request.PedidoRequestDTO;
import ar.com.wolfforge.dto.response.PedidoResponseDTO;
import ar.com.wolfforge.enums.EstadoPedido;

import java.util.List;

public interface PedidoService {

    // Crear un nuevo pedido
    PedidoResponseDTO crearPedido(PedidoRequestDTO pedidoRequest);

    // Obtener un pedido por ID
    PedidoResponseDTO obtenerPedidoPorId(Long id);

    // Listar todos los pedidos (solo ADMIN)
    List<PedidoResponseDTO> listarTodosPedidos();

    // Listar pedidos de un usuario específico
    List<PedidoResponseDTO> listarPedidosPorUsuario(Long idUsuario);

    // Obtener historial de pedidos del usuario autenticado
    List<PedidoResponseDTO> obtenerMisPedidos();

    // Actualizar estado de un pedido (ADMIN)
    PedidoResponseDTO actualizarEstadoPedido(Long idPedido, Long idHerramientaPedido, EstadoPedido nuevoEstado);

    // El usuario confirma que recibió su pedido (ENVIANDO -> ENTREGADO)
    PedidoResponseDTO confirmarRecepcion(Long idPedido, Long idHerramientaPedido);

    // El usuario avisa que quiere devolver un producto ya entregado (no cambia el estado solo)
    PedidoResponseDTO solicitarDevolucion(Long idPedido, Long idHerramientaPedido);

    // El admin aprueba la devolución (ENTREGADO -> DEVUELTO), repone stock
    PedidoResponseDTO marcarDevuelto(Long idPedido, Long idHerramientaPedido);

    // Cancelar un pedido
    PedidoResponseDTO cancelarPedido(Long idPedido);

    // Eliminar un pedido (solo ADMIN)
    void eliminarPedido(Long id);
}
