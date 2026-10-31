package ar.com.wolfforge.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Data
@Entity //Esta clase se guarda como una tabla en la base de datos
@Table(name = "herramientas") //Nombre de la tabla
public class Herramienta {

    @Id //Clase primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Autoincremental en base de datos
    @Column(name = "id_herramienta")
    private Long idHerramienta;

    @Column(name = "nombre", nullable = false, length = 150) //Nombre de columna, no puede ser nulo y tiene longitud 150
    private String nombre;

    @Column(name = "marca", length = 100)
    private String marca;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "imagen_url")
    private String imagenUrl;

    // Fotos adicionales del producto (la principal sigue siendo imagenUrl)
    @ElementCollection
    @CollectionTable(name = "herramienta_imagenes", joinColumns = @JoinColumn(name = "id_herramienta"))
    @Column(name = "url", columnDefinition = "TEXT")
    @OrderColumn(name = "orden")
    private List<String> imagenesSecundarias;

    // Lista de características técnicas (bullets del info.txt relevado)
    @ElementCollection
    @CollectionTable(name = "herramienta_caracteristicas", joinColumns = @JoinColumn(name = "id_herramienta"))
    @Column(name = "caracteristica", columnDefinition = "TEXT")
    @OrderColumn(name = "orden")
    private List<String> caracteristicas;

    // Qué incluye el producto (texto libre, puede no existir)
    @Column(name = "incluye", columnDefinition = "TEXT")
    private String incluye;

    @Column(name = "precio", precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(name = "stock", nullable = false)
    private Integer stock = 0; // Stock disponible de la herramienta

    @Column(name = "descuento", nullable = false)
    private Integer descuento = 0; // Porcentaje de descuento (0-100). 0 = sin oferta

    //Relacion con la categoria, muchos herramientas pueden pertenecer a una misma catetgoria
    @ManyToOne(fetch = FetchType.LAZY) //La categorai se carga solo cuando se la usa(mejor rendimiento)
    @JoinColumn(name = "id_categoria") //Crea la colimna foranea id_categoria en la tabla herramientas
    private Categoria categoria;

    @CreationTimestamp //Guarda automaticamente la fecha/hora de creacion del registro
    @Column(name = "creado_en", updatable = false) //updatable = false evita que se cambie al actualizar.
    private LocalDateTime creadoEn;


    // Metodos de comparacion
    //Si ambos herramientas tienen ID, compara por ID (entidad ya guarda) si no, compara por nombre y marca
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Herramienta)) return false;
        Herramienta herramienta = (Herramienta) o;

        if (this.idHerramienta != null && herramienta.idHerramienta != null) {
            return this.idHerramienta.equals(herramienta.idHerramienta);
        }

        // Comparación por contenido si aún no está persistido
        return Objects.equals(nombre, herramienta.nombre) &&
                Objects.equals(marca, herramienta.marca);
    }

    //Usa idHerramientas si existe, si no usa nombre y marca
    @Override
    public int hashCode() {
        return Objects.hash(idHerramienta != null ? idHerramienta : nombre, marca);
    }

    //una práctica recomendada para herencias y frameworks como Hibernate
    public boolean canEqual(Object other) {
        return other instanceof Herramienta;
    }
}
