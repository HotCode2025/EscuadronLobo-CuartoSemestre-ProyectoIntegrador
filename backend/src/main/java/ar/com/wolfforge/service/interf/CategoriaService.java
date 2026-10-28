package ar.com.wolfforge.service.interf;

import ar.com.wolfforge.dto.request.CategoriaRequestDTO;
import ar.com.wolfforge.dto.response.CategoriaResponseDTO;

import java.util.List;

public interface CategoriaService {

    // Crear una categoría
    CategoriaResponseDTO crearCategoria(CategoriaRequestDTO categoriaRequest);

    // Obtener una categoría por ID
    CategoriaResponseDTO obtenerCategoriaPorId(Long id);

    // Listar todas las categorías
    List<CategoriaResponseDTO> listarCategorias();

    // Actualizar una categoría existente
    CategoriaResponseDTO actualizarCategoria(Long id, CategoriaRequestDTO categoriaRequest);

    // Eliminar una categoría
    void eliminarCategoria(Long id);
}
