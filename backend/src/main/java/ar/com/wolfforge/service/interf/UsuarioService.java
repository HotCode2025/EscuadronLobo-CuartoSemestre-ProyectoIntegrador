package ar.com.wolfforge.service.interf;

import ar.com.wolfforge.dto.request.InicioSesionRequestDTO;
import ar.com.wolfforge.dto.request.UsuarioRequestDTO;
import ar.com.wolfforge.dto.response.InicioSesionResponseDTO;
import ar.com.wolfforge.dto.response.UsuarioResponseDTO;

import java.util.List;

public interface UsuarioService {

    // Registrar nuevo usuario
    InicioSesionResponseDTO registrarUsuario(UsuarioRequestDTO registroRequest);

    // Iniciar sesión
    InicioSesionResponseDTO iniciarSesion(InicioSesionRequestDTO loginRequest);

    // Obtener todos los usuarios (solo ADMIN)
    List<UsuarioResponseDTO> obtenerTodos();

    // Obtener el usuario actualmente logueado (opcional)
    UsuarioResponseDTO obtenerUsuarioActual();
}
