package ar.com.wolfforge.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaResponseDTO {

    private Long idCategoria;
    private String nombre;
    private LocalDateTime creadoEn;
    //private List<HerramientaResponseDTO> herramientas;
}
