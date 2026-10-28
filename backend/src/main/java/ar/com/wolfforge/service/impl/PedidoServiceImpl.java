package ar.com.wolfforge.service.impl;

import ar.com.wolfforge.dto.request.HerramientaPedidoRequestDTO;
import ar.com.wolfforge.dto.request.PedidoRequestDTO;
import ar.com.wolfforge.dto.response.PedidoResponseDTO;
import ar.com.wolfforge.entity.Herramienta;
import ar.com.wolfforge.entity.HerramientaPedido;
import ar.com.wolfforge.entity.Pedido;
import ar.com.wolfforge.entity.Usuario;
import ar.com.wolfforge.enums.EstadoPedido;
import ar.com.wolfforge.exceptions.NotFoundException;
import ar.com.wolfforge.mapper.PedidoMapper;
import ar.com.wolfforge.repository.HerramientaPedidoRepository;
import ar.com.wolfforge.repository.HerramientaRepository;
import ar.com.wolfforge.repository.PedidoRepository;
import ar.com.wolfforge.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PedidoServiceImpl implements ar.com.wolfforge.service.interf.PedidoService {

    private final PedidoRepository pedidoRepository;
    private final HerramientaPedidoRepository herramientaPedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final HerramientaRepository herramientaRepository;
    private final PedidoMapper pedidoMapper;

    @Override
    public PedidoResponseDTO crearPedido(PedidoRequestDTO pedidoRequest) {
        // Validar que el usuario exista
        Usuario usuario = usuarioRepository.findById(pedidoRequest.getIdUsuario())
                .orElseThrow(() -> new NotFoundException(
                        "Usuario con ID " + pedidoRequest.getIdUsuario() + " no encontrado"
                ));

        // Validar que la lista de herramientas no esté vacía
        if (pedidoRequest.getHerramientas() == null || pedidoRequest.getHerramientas().isEmpty()) {
            throw new IllegalArgumentException("El pedido debe contener al menos una herramienta");
        }

        // Crear el pedido
        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setListaHerramientas(new ArrayList<>());

        BigDecimal precioTotal = BigDecimal.ZERO;

        // Procesar cada herramienta del pedido
        for (HerramientaPedidoRequestDTO herramientaDTO : pedidoRequest.getHerramientas()) {
            // Buscar la herramienta
            Herramienta herramienta = herramientaRepository.findById(herramientaDTO.getIdHerramienta())
                    .orElseThrow(() -> new NotFoundException(
                            "Herramienta con ID " + herramientaDTO.getIdHerramienta() + " no encontrado"
                    ));

            // Validar stock disponible
            if (herramienta.getStock() < herramientaDTO.getCantidad()) {
                throw new IllegalArgumentException(
                        "Stock insuficiente para la herramienta '" + herramienta.getNombre() +
                        "'. Disponible: " + herramienta.getStock() + ", Solicitado: " + herramientaDTO.getCantidad()
                );
            }

            // Descontar stock
            herramienta.setStock(herramienta.getStock() - herramientaDTO.getCantidad());
            herramientaRepository.save(herramienta);

            // Crear HerramientaPedido
            HerramientaPedido herramientaPedido = new HerramientaPedido();
            herramientaPedido.setHerramienta(herramienta);
            herramientaPedido.setUsuario(usuario);
            herramientaPedido.setPedido(pedido);
            herramientaPedido.setCantidad(herramientaDTO.getCantidad());
            herramientaPedido.setEstadoPedido(EstadoPedido.PENDIENTE);

            // Agregar a la lista del pedido
            pedido.getListaHerramientas().add(herramientaPedido);

            // Calcular precio total
            BigDecimal precioHerramienta = herramienta.getPrecio().multiply(BigDecimal.valueOf(herramientaDTO.getCantidad()));
            precioTotal = precioTotal.add(precioHerramienta);
        }

        // Establecer precio total
        pedido.setPrecioTotal(precioTotal);

        // Guardar el pedido (cascade guardará también los HerramientaPedido)
        pedido = pedidoRepository.save(pedido);

        return pedidoMapper.toResponseDTO(pedido);
    }

    @Override
    public PedidoResponseDTO obtenerPedidoPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pedido con ID " + id + " no encontrado"));
        return pedidoMapper.toResponseDTO(pedido);
    }

    @Override
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public List<PedidoResponseDTO> listarTodosPedidos() {
        return pedidoRepository.findAll()
                .stream()
                .map(pedidoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PedidoResponseDTO> listarPedidosPorUsuario(Long idUsuario) {
        // Verificar que el usuario exista
        if (!usuarioRepository.existsById(idUsuario)) {
            throw new NotFoundException("Usuario con ID " + idUsuario + " no encontrado");
        }

        return pedidoRepository.findByUsuarioIdUsuario(idUsuario)
                .stream()
                .map(pedidoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PedidoResponseDTO> obtenerMisPedidos() {
        // Obtener el usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        return pedidoRepository.findByUsuarioIdUsuario(usuario.getIdUsuario())
                .stream()
                .map(pedidoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PedidoResponseDTO actualizarEstadoPedido(Long idPedido, Long idHerramientaPedido, EstadoPedido nuevoEstado) {
        // Buscar el pedido
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new NotFoundException("Pedido con ID " + idPedido + " no encontrado"));

        // Buscar el HerramientaPedido específico
        HerramientaPedido herramientaPedido = herramientaPedidoRepository.findById(idHerramientaPedido)
                .orElseThrow(() -> new NotFoundException("HerramientaPedido con ID " + idHerramientaPedido + " no encontrado"));

        // Verificar que el HerramientaPedido pertenezca al Pedido
        if (!herramientaPedido.getPedido().getIdPedido().equals(idPedido)) {
            throw new IllegalArgumentException("El HerramientaPedido no pertenece al Pedido especificado");
        }

        // Actualizar estado
        herramientaPedido.setEstadoPedido(nuevoEstado);
        herramientaPedidoRepository.save(herramientaPedido);

        // Devolver el pedido actualizado
        return pedidoMapper.toResponseDTO(pedido);
    }

    @Override
    public PedidoResponseDTO confirmarRecepcion(Long idPedido, Long idHerramientaPedido) {
        // Obtener el usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        // Buscar el pedido
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new NotFoundException("Pedido con ID " + idPedido + " no encontrado"));

        // Verificar que el pedido pertenezca al usuario autenticado
        if (!pedido.getUsuario().getIdUsuario().equals(usuario.getIdUsuario())) {
            throw new IllegalStateException("Este pedido no te pertenece");
        }

        // Buscar el HerramientaPedido específico
        HerramientaPedido herramientaPedido = herramientaPedidoRepository.findById(idHerramientaPedido)
                .orElseThrow(() -> new NotFoundException("HerramientaPedido con ID " + idHerramientaPedido + " no encontrado"));

        // Verificar que el HerramientaPedido pertenezca al Pedido
        if (!herramientaPedido.getPedido().getIdPedido().equals(idPedido)) {
            throw new IllegalArgumentException("El HerramientaPedido no pertenece al Pedido especificado");
        }

        // Solo se puede confirmar recepción si está en camino
        if (herramientaPedido.getEstadoPedido() != EstadoPedido.ENVIANDO) {
            throw new IllegalStateException(
                "Solo se puede confirmar la recepción de un producto que está en estado ENVIANDO (estado actual: "
                    + herramientaPedido.getEstadoPedido() + ")"
            );
        }

        herramientaPedido.setEstadoPedido(EstadoPedido.ENTREGADO);
        herramientaPedidoRepository.save(herramientaPedido);

        return pedidoMapper.toResponseDTO(pedido);
    }

    @Override
    public PedidoResponseDTO solicitarDevolucion(Long idPedido, Long idHerramientaPedido) {
        // Obtener el usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new NotFoundException("Pedido con ID " + idPedido + " no encontrado"));

        if (!pedido.getUsuario().getIdUsuario().equals(usuario.getIdUsuario())) {
            throw new IllegalStateException("Este pedido no te pertenece");
        }

        HerramientaPedido herramientaPedido = herramientaPedidoRepository.findById(idHerramientaPedido)
                .orElseThrow(() -> new NotFoundException("HerramientaPedido con ID " + idHerramientaPedido + " no encontrado"));

        if (!herramientaPedido.getPedido().getIdPedido().equals(idPedido)) {
            throw new IllegalArgumentException("El HerramientaPedido no pertenece al Pedido especificado");
        }

        if (herramientaPedido.getEstadoPedido() != EstadoPedido.ENTREGADO) {
            throw new IllegalStateException(
                "Solo se puede solicitar la devolución de un producto que ya fue ENTREGADO (estado actual: "
                    + herramientaPedido.getEstadoPedido() + ")"
            );
        }

        herramientaPedido.setSolicitudDevolucion(true);
        herramientaPedidoRepository.save(herramientaPedido);

        return pedidoMapper.toResponseDTO(pedido);
    }

    @Override
    public PedidoResponseDTO marcarDevuelto(Long idPedido, Long idHerramientaPedido) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new NotFoundException("Pedido con ID " + idPedido + " no encontrado"));

        HerramientaPedido herramientaPedido = herramientaPedidoRepository.findById(idHerramientaPedido)
                .orElseThrow(() -> new NotFoundException("HerramientaPedido con ID " + idHerramientaPedido + " no encontrado"));

        if (!herramientaPedido.getPedido().getIdPedido().equals(idPedido)) {
            throw new IllegalArgumentException("El HerramientaPedido no pertenece al Pedido especificado");
        }

        if (herramientaPedido.getEstadoPedido() != EstadoPedido.ENTREGADO) {
            throw new IllegalStateException(
                "Solo se puede marcar como devuelto un producto que está ENTREGADO (estado actual: "
                    + herramientaPedido.getEstadoPedido() + ")"
            );
        }

        // Reponer stock
        Herramienta herramienta = herramientaPedido.getHerramienta();
        herramienta.setStock(herramienta.getStock() + herramientaPedido.getCantidad());
        herramientaRepository.save(herramienta);

        herramientaPedido.setEstadoPedido(EstadoPedido.DEVUELTO);
        herramientaPedido.setSolicitudDevolucion(false);
        herramientaPedidoRepository.save(herramientaPedido);

        return pedidoMapper.toResponseDTO(pedido);
    }

    @Override
    public PedidoResponseDTO cancelarPedido(Long idPedido) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new NotFoundException("Pedido con ID " + idPedido + " no encontrado"));

        // Cancelar todos los HerramientaPedido del pedido y restaurar stock
        for (HerramientaPedido herramientaPedido : pedido.getListaHerramientas()) {
            if (herramientaPedido.getEstadoPedido() == EstadoPedido.PENDIENTE ||
                herramientaPedido.getEstadoPedido() == EstadoPedido.CONFIRMADO) {

                // Restaurar stock de la herramienta
                Herramienta herramienta = herramientaPedido.getHerramienta();
                herramienta.setStock(herramienta.getStock() + herramientaPedido.getCantidad());
                herramientaRepository.save(herramienta);

                // Cambiar estado a cancelado
                herramientaPedido.setEstadoPedido(EstadoPedido.CANCELADO);
            } else {
                throw new IllegalArgumentException(
                        "No se puede cancelar el pedido. Algunos items ya están en proceso de envío o entregados."
                );
            }
        }

        pedido = pedidoRepository.save(pedido);
        return pedidoMapper.toResponseDTO(pedido);
    }

    @Override
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public void eliminarPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pedido con ID " + id + " no encontrado"));

        pedidoRepository.delete(pedido);
    }
}
