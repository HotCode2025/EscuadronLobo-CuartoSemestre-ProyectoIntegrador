package ar.com.wolfforge.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HerramientaResponseDTO {

    private Long idHerramienta;
    private String nombre;
    private String marca;
    private String descripcion;
    private String imagenUrl;
    private BigDecimal precio;
    private Integer stock;
    private LocalDateTime creadoEn;
    protected CategoriaResponseDTO categoria;
    private List<String> imagenesSecundarias;
    private List<String> caracteristicas;
    private String incluye;
    private Integer descuento;
}
