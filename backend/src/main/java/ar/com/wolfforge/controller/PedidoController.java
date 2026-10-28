package ar.com.wolfforge.controller;

import ar.com.wolfforge.dto.request.PedidoRequestDTO;
import ar.com.wolfforge.dto.response.PedidoResponseDTO;
import ar.com.wolfforge.enums.EstadoPedido;
import ar.com.wolfforge.service.interf.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    // ================= CREAR PEDIDO =================
    @PostMapping("/crear")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'USUARIO')")
    public ResponseEntity<PedidoResponseDTO> crearPedido(@RequestBody PedidoRequestDTO pedidoRequest) {
        PedidoResponseDTO pedidoCreado = pedidoService.crearPedido(pedidoRequest);
        return ResponseEntity.ok(pedidoCreado);
    }

    // ================= LISTAR TODOS LOS PEDIDOS (SOLO ADMIN) =================
    @GetMapping("/listar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<PedidoResponseDTO>> listarTodosPedidos() {
        return ResponseEntity.ok(pedidoService.listarTodosPedidos());
    }

    // ================= OBTENER MIS PEDIDOS (USUARIO AUTENTICADO) =================
    @GetMapping("/mis-pedidos")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'USUARIO')")
    public ResponseEntity<List<PedidoResponseDTO>> obtenerMisPedidos() {
        return ResponseEntity.ok(pedidoService.obtenerMisPedidos());
    }

    // ================= ACTUALIZAR ESTADO DE UN PEDIDO (ADMIN) =================
    @PutMapping("/actualizar-estado/{idPedido}/herramienta-pedido/{idHerramientaPedido}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<PedidoResponseDTO> actualizarEstadoPedido(
            @PathVariable Long idPedido,
            @PathVariable Long idHerramientaPedido,
            @RequestParam EstadoPedido nuevoEstado) {
        PedidoResponseDTO pedidoActualizado = pedidoService.actualizarEstadoPedido(idPedido, idHerramientaPedido, nuevoEstado);
        return ResponseEntity.ok(pedidoActualizado);
    }

    // ================= CONFIRMAR RECEPCIÓN (USUARIO) =================
    // El comprador confirma que recibió el producto: ENVIANDO -> ENTREGADO
    @PutMapping("/{idPedido}/herramienta-pedido/{idHerramientaPedido}/confirmar-recepcion")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'USUARIO')")
    public ResponseEntity<PedidoResponseDTO> confirmarRecepcion(
            @PathVariable Long idPedido,
            @PathVariable Long idHerramientaPedido) {
        PedidoResponseDTO pedidoActualizado = pedidoService.confirmarRecepcion(idPedido, idHerramientaPedido);
        return ResponseEntity.ok(pedidoActualizado);
    }

    // ================= SOLICITAR DEVOLUCIÓN (USUARIO) =================
    // El cliente avisa que quiere devolver un producto ya entregado. No cambia el estado, solo avisa.
    @PutMapping("/{idPedido}/herramienta-pedido/{idHerramientaPedido}/solicitar-devolucion")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'USUARIO')")
    public ResponseEntity<PedidoResponseDTO> solicitarDevolucion(
            @PathVariable Long idPedido,
            @PathVariable Long idHerramientaPedido) {
        PedidoResponseDTO pedidoActualizado = pedidoService.solicitarDevolucion(idPedido, idHerramientaPedido);
        return ResponseEntity.ok(pedidoActualizado);
    }

    // ================= MARCAR DEVUELTO (ADMIN) =================
    // El admin aprueba la devolución: ENTREGADO -> DEVUELTO, repone stock.
    @PutMapping("/{idPedido}/herramienta-pedido/{idHerramientaPedido}/marcar-devuelto")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<PedidoResponseDTO> marcarDevuelto(
            @PathVariable Long idPedido,
            @PathVariable Long idHerramientaPedido) {
        PedidoResponseDTO pedidoActualizado = pedidoService.marcarDevuelto(idPedido, idHerramientaPedido);
        return ResponseEntity.ok(pedidoActualizado);
    }

    // ================= CANCELAR PEDIDO =================
    @PutMapping("/cancelar/{idPedido}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'USUARIO')")
    public ResponseEntity<PedidoResponseDTO> cancelarPedido(@PathVariable Long idPedido) {
        PedidoResponseDTO pedidoCancelado = pedidoService.cancelarPedido(idPedido);
        return ResponseEntity.ok(pedidoCancelado);
    }
}
