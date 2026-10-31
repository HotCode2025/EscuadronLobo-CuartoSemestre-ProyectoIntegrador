package ar.com.wolfforge.repository;

import ar.com.wolfforge.entity.Herramienta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HerramientaRepository extends JpaRepository<Herramienta, Long> {

    // Buscar herramienta por título
    Optional<Herramienta> findByNombre(String nombre);

    // Buscar herramientas por marca
    List<Herramienta> findByMarca(String marca);

    // Buscar herramientas por categoría
    List<Herramienta> findByCategoriaIdCategoria(Long idCategoria);

    // Verificar si ya existe una herramienta por título y marca
    boolean existsByNombreAndMarca(String nombre, String marca);

    // Buscar herramientas por texto en título o marca (optimizado con query)
    @Query("SELECT l FROM Herramienta l WHERE " +
           "LOWER(l.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(l.marca) LIKE LOWER(CONCAT('%', :texto, '%'))")
    List<Herramienta> buscarPorTexto(@Param("texto") String texto);

    // Buscar herramientas en oferta (con descuento activo)
    List<Herramienta> findByDescuentoGreaterThan(Integer descuento);
}
