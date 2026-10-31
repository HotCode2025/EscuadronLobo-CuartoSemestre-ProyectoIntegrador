package ar.com.wolfforge.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HerramientaRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El marca es obligatorio")
    private String marca;

    private String descripcion;

    private String imagenUrl; // URL de imagen (opcional - si no se proporciona, usa default)

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor que 0")
    private BigDecimal precio;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock; // Stock disponible de la herramienta

    @NotNull(message = "Debe indicar el ID de la categoría")
    private Long idCategoria; // se recibe el ID de la categoría asociada

    @Min(value = 0, message = "El descuento no puede ser negativo")
    @Max(value = 100, message = "El descuento no puede ser mayor a 100")
    private Integer descuento; // Porcentaje de descuento (0-100), opcional. Null/0 = sin oferta

    private List<String> caracteristicas; // Bullets de características técnicas, opcional
}
