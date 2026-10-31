package ar.com.wolfforge.entity;

import ar.com.wolfforge.enums.EstadoPedido;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@Entity
@Table(name = "herramienta_pedidos")
public class HerramientaPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_herramienta_pedido")
    private Long idHerramientaPedido;

    @Column(nullable = false, name = "cantidad")
    private int cantidad;

    @Enumerated(EnumType.STRING) //Guarda el nombre del enum no el numero.
    @Column(name = "estado_pedido", length = 30)
    private EstadoPedido estadoPedido;

    // El cliente marcó que quiere devolver este producto (solo avisa, no cambia el estado solo)
    @Column(name = "solicitud_devolucion", nullable = false)
    private boolean solicitudDevolucion = false;

    //Relaciones entre entidades

    @ManyToOne(fetch = FetchType.LAZY) //Evita sobrecargar consultas
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_herramienta")
    private Herramienta herramienta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido")
    private Pedido pedido;

    @CreationTimestamp //Se asigna automáticamente al crear el registro (sin necesidad de LocalDateTime.now() manual)
    @Column(name = "creado_en", updatable = false) //updatable = false evita que se cambie al actualizar.
    private LocalDateTime creadoEn;


    //Metodos de comparacion
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HerramientaPedido)) return false;
        HerramientaPedido that = (HerramientaPedido) o;

        // Si ambos tienen ID (ya persistidos)
        if (this.idHerramientaPedido != null && that.idHerramientaPedido != null) {
            return this.idHerramientaPedido.equals(that.idHerramientaPedido);
        }

        // Comparación por contenido si aún no se persistió
        return cantidad == that.cantidad &&
                Objects.equals(herramienta, that.herramienta) &&
                Objects.equals(usuario, that.usuario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idHerramientaPedido != null ? idHerramientaPedido : herramienta, cantidad, usuario);
    }

    public boolean canEqual(Object other) {
        return other instanceof HerramientaPedido;
    }
}
