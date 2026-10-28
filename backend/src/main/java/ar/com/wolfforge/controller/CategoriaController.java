package ar.com.wolfforge.controller;

import ar.com.wolfforge.dto.response.CategoriaResponseDTO;
import ar.com.wolfforge.service.interf.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    // ================= LISTAR TODAS LAS CATEGORÍAS =================
    @GetMapping("/listar")
    public ResponseEntity<List<CategoriaResponseDTO>> listarCategorias() {
        return ResponseEntity.ok(categoriaService.listarCategorias());
    }
}
