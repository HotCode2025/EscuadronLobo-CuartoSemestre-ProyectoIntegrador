package ar.com.wolfforge.security;

import ar.com.wolfforge.entity.Usuario;
import ar.com.wolfforge.exceptions.NotFoundException;
import ar.com.wolfforge.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService { // Se conecta con tu UsuarioRepository

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuario con email " + email + " no encontrado"));
        return AuthUser.builder()
                .usuario(usuario)
                .build();
    }
}
