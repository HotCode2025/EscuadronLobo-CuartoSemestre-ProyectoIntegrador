package ar.com.wolfforge.service.impl;

import ar.com.wolfforge.dto.request.HerramientaRequestDTO;
import ar.com.wolfforge.dto.response.HerramientaResponseDTO;
import ar.com.wolfforge.exceptions.NotFoundException;
import ar.com.wolfforge.mapper.HerramientaMapper;
import ar.com.wolfforge.repository.CategoriaRepository;
import ar.com.wolfforge.repository.HerramientaRepository;
import ar.com.wolfforge.service.interf.HerramientaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class HerramientaServiceImpl implements HerramientaService {

    private final HerramientaRepository herramientaRepository;
    private final CategoriaRepository categoriaRepository;
    private final HerramientaMapper herramientaMapper;

    @Override
    public HerramientaResponseDTO crearHerramienta(HerramientaRequestDTO herramientaRequest) {
        // Buscar categoría
        var categoria = categoriaRepository.findById(herramientaRequest.getIdCategoria())
                .orElseThrow(() -> new NotFoundException(
                        "Categoría con ID " + herramientaRequest.getIdCategoria() + " no encontrada"
                ));

        // Convertir DTO a entidad
        var herramienta = herramientaMapper.toEntity(herramientaRequest, categoria);

        // Usar URL de imagen del DTO o imagen por defecto
        if (herramientaRequest.getImagenUrl() != null && !herramientaRequest.getImagenUrl().isEmpty()) {
            herramienta.setImagenUrl(herramientaRequest.getImagenUrl());
        } else {
            herramienta.setImagenUrl("/uploads/herramientas/default-book.png");
        }

        // Guardar herramienta
        herramienta = herramientaRepository.save(herramienta);

        return herramientaMapper.toResponseDTO(herramienta);
    }

    @Override
    public HerramientaResponseDTO obtenerHerramientaPorId(Long id) {
        var herramienta = herramientaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Herramienta con ID " + id + " no encontrado"));
        return herramientaMapper.toResponseDTO(herramienta);
    }

    @Override
    public List<HerramientaResponseDTO> listarHerramientas() {
        return herramientaRepository.findAll()
                .stream()
                .map(herramientaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public HerramientaResponseDTO actualizarHerramienta(Long id, HerramientaRequestDTO herramientaRequest) {
        var herramientaExistente = herramientaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Herramienta con ID " + id + " no encontrado"));

        var categoria = categoriaRepository.findById(herramientaRequest.getIdCategoria())
                .orElseThrow(() -> new NotFoundException(
                        "Categoría con ID " + herramientaRequest.getIdCategoria() + " no encontrada"
                ));

        // Actualizar datos de la herramienta
        herramientaMapper.updateEntity(herramientaExistente, herramientaRequest, categoria);

        // Actualizar URL de imagen si se proporciona
        if (herramientaRequest.getImagenUrl() != null && !herramientaRequest.getImagenUrl().isEmpty()) {
            herramientaExistente.setImagenUrl(herramientaRequest.getImagenUrl());
        }

        herramientaExistente = herramientaRepository.save(herramientaExistente);

        return herramientaMapper.toResponseDTO(herramientaExistente);
    }

    @Override
    public void eliminarHerramienta(Long id) {
        var herramienta = herramientaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Herramienta con ID " + id + " no encontrado"));

        // Solo eliminar el registro de la base de datos
        // La imagen es una URL externa, no necesita ser eliminada
        // Si hay conflicto de integridad referencial, el GlobalExceptionHandler lo maneja
        herramientaRepository.delete(herramienta);
    }

    @Override
    public List<HerramientaResponseDTO> buscarPorTexto(String texto) {
        return herramientaRepository.buscarPorTexto(texto)
                .stream()
                .map(herramientaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<HerramientaResponseDTO> obtenerPorCategoria(Long idCategoria) {
        return herramientaRepository.findByCategoriaIdCategoria(idCategoria)
                .stream()
                .map(herramientaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<HerramientaResponseDTO> crearHerramientasLote(List<HerramientaRequestDTO> herramientasRequest) {
        return herramientasRequest.stream()
                .map(this::crearHerramienta)
                .collect(Collectors.toList());
    }

    @Override
    public List<HerramientaResponseDTO> obtenerOfertas() {
        return herramientaRepository.findByDescuentoGreaterThan(0)
                .stream()
                .map(herramientaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
