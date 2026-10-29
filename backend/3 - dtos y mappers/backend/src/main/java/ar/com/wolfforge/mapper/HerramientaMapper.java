package ar.com.wolfforge.mapper;

import ar.com.wolfforge.dto.request.HerramientaRequestDTO;
import ar.com.wolfforge.dto.response.CategoriaResponseDTO;
import ar.com.wolfforge.dto.response.HerramientaResponseDTO;
import ar.com.wolfforge.entity.Categoria;
import ar.com.wolfforge.entity.Herramienta;
import org.springframework.stereotype.Component;

@Component
public class HerramientaMapper {

    // Convierte de entidad a DTO de respuesta
    public HerramientaResponseDTO toResponseDTO(Herramienta herramienta) {
        if (herramienta == null) return null;

        HerramientaResponseDTO dto = new HerramientaResponseDTO();
        dto.setIdHerramienta(herramienta.getIdHerramienta());
        dto.setNombre(herramienta.getNombre());
        dto.setMarca(herramienta.getMarca());
        dto.setDescripcion(herramienta.getDescripcion());
        dto.setImagenUrl(herramienta.getImagenUrl());
        dto.setPrecio(herramienta.getPrecio());
        dto.setStock(herramienta.getStock());
        // Convertimos LocalDateTime a BigDecimal no tiene sentido, debería ser LocalDateTime
        dto.setCreadoEn(herramienta.getCreadoEn());
        dto.setImagenesSecundarias(herramienta.getImagenesSecundarias());
        dto.setCaracteristicas(herramienta.getCaracteristicas());
        dto.setIncluye(herramienta.getIncluye());
        dto.setDescuento(herramienta.getDescuento());

        if (herramienta.getCategoria() != null) {
            CategoriaResponseDTO categoriaDTO = new CategoriaResponseDTO();
            categoriaDTO.setIdCategoria(herramienta.getCategoria().getIdCategoria());
            categoriaDTO.setNombre(herramienta.getCategoria().getNombre());
            dto.setCategoria(categoriaDTO);
        }

        return dto;
    }

    // Convierte de DTO de request a entidad
    public Herramienta toEntity(HerramientaRequestDTO dto, Categoria categoria) {
        if (dto == null) return null;

        Herramienta herramienta = new Herramienta();
        herramienta.setNombre(dto.getNombre());
        herramienta.setMarca(dto.getMarca());
        herramienta.setDescripcion(dto.getDescripcion());
        herramienta.setPrecio(dto.getPrecio());
        herramienta.setStock(dto.getStock());
        herramienta.setDescuento(dto.getDescuento() != null ? dto.getDescuento() : 0);
        herramienta.setCategoria(categoria); // Recibimos la categoría ya cargada desde la base de datos
        herramienta.setCaracteristicas(dto.getCaracteristicas());

        return herramienta;
    }

    // Actualiza una entidad existente con datos del DTO
    public void updateEntity(Herramienta herramienta, HerramientaRequestDTO dto, Categoria categoria) {
        herramienta.setNombre(dto.getNombre());
        herramienta.setMarca(dto.getMarca());
        herramienta.setDescripcion(dto.getDescripcion());
        herramienta.setPrecio(dto.getPrecio());
        herramienta.setStock(dto.getStock());
        herramienta.setDescuento(dto.getDescuento() != null ? dto.getDescuento() : 0);
        herramienta.setCategoria(categoria);
        herramienta.setCaracteristicas(dto.getCaracteristicas());
    }
}

