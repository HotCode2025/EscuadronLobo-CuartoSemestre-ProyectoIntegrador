package ar.com.wolfforge.service.interf;

import ar.com.wolfforge.dto.request.HerramientaRequestDTO;
import ar.com.wolfforge.dto.response.HerramientaResponseDTO;

import java.util.List;

public interface HerramientaService {

    // Crear una herramienta (con URL de imagen)
    HerramientaResponseDTO crearHerramienta(HerramientaRequestDTO herramientaRequest);

    // Obtener una herramienta por ID
    HerramientaResponseDTO obtenerHerramientaPorId(Long id);

    // Listar todas las herramientas
    List<HerramientaResponseDTO> listarHerramientas();

    // Actualizar una herramienta existente (con URL de imagen)
    HerramientaResponseDTO actualizarHerramienta(Long id, HerramientaRequestDTO herramientaRequest);

    // Eliminar una herramienta
    void eliminarHerramienta(Long id);

    // Buscar herramientas por texto en título o marca
    List<HerramientaResponseDTO> buscarPorTexto(String texto);

    // Obtener herramientas por categoría
    List<HerramientaResponseDTO> obtenerPorCategoria(Long idCategoria);

    // Crear múltiples herramientas (carga masiva con URLs)
    List<HerramientaResponseDTO> crearHerramientasLote(List<HerramientaRequestDTO> herramientasRequest);

    // Obtener herramientas en oferta (con descuento activo)
    List<HerramientaResponseDTO> obtenerOfertas();
}
