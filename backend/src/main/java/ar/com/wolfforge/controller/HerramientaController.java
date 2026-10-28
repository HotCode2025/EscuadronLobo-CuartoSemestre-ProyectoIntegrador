package ar.com.wolfforge.controller;

import ar.com.wolfforge.dto.request.HerramientaRequestDTO;
import ar.com.wolfforge.dto.response.HerramientaResponseDTO;
import ar.com.wolfforge.service.interf.HerramientaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/herramientas")
@RequiredArgsConstructor
public class HerramientaController {

    private final HerramientaService herramientaService;

    // ================= CREAR HERRAMIENTA (con URL de imagen) =================
    @PostMapping("/crear")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<HerramientaResponseDTO> crearHerramienta(@Valid @RequestBody HerramientaRequestDTO herramientaRequest) {
        HerramientaResponseDTO herramientaCreado = herramientaService.crearHerramienta(herramientaRequest);
        return ResponseEntity.ok(herramientaCreado);
    }

    // ================= ACTUALIZAR HERRAMIENTA =================
    @PutMapping("/actualizar/{idHerramienta}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<HerramientaResponseDTO> actualizarHerramienta(
            @PathVariable Long idHerramienta,
            @Valid @RequestBody HerramientaRequestDTO herramientaRequest
    ) {
        HerramientaResponseDTO herramientaActualizado = herramientaService.actualizarHerramienta(idHerramienta, herramientaRequest);
        return ResponseEntity.ok(herramientaActualizado);
    }

    // ================= ELIMINAR HERRAMIENTA =================
    @DeleteMapping("/eliminar/{idHerramienta}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<String> eliminarHerramienta(@PathVariable Long idHerramienta){
        herramientaService.eliminarHerramienta(idHerramienta);
        return ResponseEntity.ok("Herramienta eliminada correctamente");
    }

    // ================= OBTENER HERRAMIENTA POR ID (PÚBLICO - ficha de producto) =================
    @GetMapping("/obtener/{idHerramienta}")
    public ResponseEntity<HerramientaResponseDTO> obtenerHerramientaPorId(@PathVariable Long idHerramienta){
        return ResponseEntity.ok(herramientaService.obtenerHerramientaPorId(idHerramienta));
    }

    // ================= LISTAR TODOS LAS HERRAMIENTAS (PÚBLICO - Catálogo) =================
    @GetMapping("/listar")
    public ResponseEntity<List<HerramientaResponseDTO>> listarHerramientas(){
        return ResponseEntity.ok(herramientaService.listarHerramientas());
    }

    // ================= OFERTAS (PÚBLICO - productos con descuento activo) =================
    @GetMapping("/ofertas")
    public ResponseEntity<List<HerramientaResponseDTO>> obtenerOfertas(){
        return ResponseEntity.ok(herramientaService.obtenerOfertas());
    }
}
