package ar.com.wolfforge.repository;

import ar.com.wolfforge.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    // Buscar categoría por nombre (opcional)
    Optional<Categoria> findByNombre(String nombre);

    // Verifica si existe una categoría por nombre
    boolean existsByNombre(String nombre);
}
