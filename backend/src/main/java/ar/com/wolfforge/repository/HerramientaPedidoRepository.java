package ar.com.wolfforge.repository;

import ar.com.wolfforge.entity.HerramientaPedido;
import ar.com.wolfforge.enums.EstadoPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HerramientaPedidoRepository extends JpaRepository<HerramientaPedido, Long> {

    // Buscar todos las herramientas pedidas por un usuario
    List<HerramientaPedido> findByUsuarioIdUsuario(Long idUsuario);

    // Buscar todas las herramientas de un pedido específico
    List<HerramientaPedido> findByPedidoIdPedido(Long idPedido);

    // Buscar por estado del pedido
    List<HerramientaPedido> findByEstadoPedido(EstadoPedido estadoPedido);

    // Buscar herramientas pedidos por usuario y estado
    List<HerramientaPedido> findByUsuarioIdUsuarioAndEstadoPedido(Long idUsuario, EstadoPedido estadoPedido);
}
