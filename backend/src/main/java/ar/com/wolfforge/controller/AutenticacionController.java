package ar.com.wolfforge.controller;

import ar.com.wolfforge.dto.request.InicioSesionRequestDTO;
import ar.com.wolfforge.dto.request.UsuarioRequestDTO;
import ar.com.wolfforge.dto.response.InicioSesionResponseDTO;
import ar.com.wolfforge.service.interf.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AutenticacionController {

    private final UsuarioService usuarioService;

    public AutenticacionController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/register")
    public ResponseEntity<InicioSesionResponseDTO> registrarUsuario(
            @Valid @RequestBody UsuarioRequestDTO request) {
        InicioSesionResponseDTO response = usuarioService.registrarUsuario(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<InicioSesionResponseDTO> iniciarSesion(
            @Valid @RequestBody InicioSesionRequestDTO request) {
        InicioSesionResponseDTO response = usuarioService.iniciarSesion(request);
        return ResponseEntity.ok(response);
    }
}
