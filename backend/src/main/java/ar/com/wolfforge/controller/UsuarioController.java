package ar.com.wolfforge.controller;

import ar.com.wolfforge.dto.response.UsuarioResponseDTO;
import ar.com.wolfforge.service.interf.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    // Endpoint para obtener la info del usuario logueado
    @GetMapping("/mi-info")
    public ResponseEntity<UsuarioResponseDTO> obtenerUsuarioActual() {
        UsuarioResponseDTO usuario = usuarioService.obtenerUsuarioActual();
        return ResponseEntity.ok(usuario);
    }
}
