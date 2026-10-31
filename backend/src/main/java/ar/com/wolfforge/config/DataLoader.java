package ar.com.wolfforge.config;

import ar.com.wolfforge.entity.Categoria;
import ar.com.wolfforge.entity.Direccion;
import ar.com.wolfforge.entity.Herramienta;
import ar.com.wolfforge.entity.Usuario;
import ar.com.wolfforge.enums.UsuarioRol;
import ar.com.wolfforge.repository.CategoriaRepository;
import ar.com.wolfforge.repository.HerramientaRepository;
import ar.com.wolfforge.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final CategoriaRepository categoriaRepository;
    private final HerramientaRepository herramientaRepository;
    private final UsuarioRepository usuarioRepository; // <-- agregar
    private final PasswordEncoder passwordEncoder;     // <-- para cifrar la contraseña
    private final ObjectMapper objectMapper;

    @Override
    public void run(String... args) throws Exception {
        // Solo cargar datos si la base de datos está vacía
        if (herramientaRepository.count() == 0 && categoriaRepository.count() == 0) {
            log.info("La base de datos está vacía. Cargando datos iniciales...");
            loadInitialData();
        } else {
            log.info("La base de datos ya contiene datos. Omitiendo carga inicial.");
        }
    }

    private void loadInitialData() {
        try {
            ClassPathResource resource = new ClassPathResource("data/herramientas-iniciales.json");
            JsonNode rootNode = objectMapper.readTree(resource.getInputStream());

            // Lista para almacenar las categorías en orden (índice = idCategoria - 1)
            Map<Integer, Categoria> categoriasMap = new HashMap<>();

            // Primero, cargar las categorías
            JsonNode categoriasNode = rootNode.get("categorias");
            if (categoriasNode != null && categoriasNode.isArray()) {
                int index = 1;
                for (JsonNode categoriaNode : categoriasNode) {
                    String nombreCategoria = categoriaNode.get("nombre").asText();

                    // Verificar si la categoría ya existe
                    Categoria categoria = categoriaRepository.findByNombre(nombreCategoria)
                            .orElseGet(() -> {
                                Categoria nuevaCategoria = new Categoria();
                                nuevaCategoria.setNombre(nombreCategoria);
                                return categoriaRepository.save(nuevaCategoria);
                            });

                    categoriasMap.put(index, categoria);
                    log.info("Categoría {} cargada/verificada: {}", index, nombreCategoria);
                    index++;
                }
            }

            // Luego, cargar las herramientas
            JsonNode herramientasNode = rootNode.get("herramientas");
            if (herramientasNode != null && herramientasNode.isArray()) {
                for (JsonNode herramientaNode : herramientasNode) {
                    String nombre = herramientaNode.get("nombre").asText();

                    // Verificar si la herramienta ya existe
                    if (herramientaRepository.findByNombre(nombre).isEmpty()) {
                        Herramienta herramienta = new Herramienta();
                        herramienta.setNombre(nombre);
                        herramienta.setMarca(herramientaNode.get("marca").asText());
                        herramienta.setDescripcion(herramientaNode.get("descripcion").asText());
                        herramienta.setImagenUrl(herramientaNode.get("imagenUrl").asText());
                        herramienta.setPrecio(new BigDecimal(herramientaNode.get("precio").asInt()));
                        herramienta.setStock(herramientaNode.get("stock").asInt());

                        // Campos opcionales: fotos adicionales, características e incluye
                        JsonNode imagenesNode = herramientaNode.get("imagenesSecundarias");
                        if (imagenesNode != null && imagenesNode.isArray()) {
                            List<String> imagenes = new ArrayList<>();
                            imagenesNode.forEach(n -> imagenes.add(n.asText()));
                            herramienta.setImagenesSecundarias(imagenes);
                        }

                        JsonNode caracteristicasNode = herramientaNode.get("caracteristicas");
                        if (caracteristicasNode != null && caracteristicasNode.isArray()) {
                            List<String> caracteristicas = new ArrayList<>();
                            caracteristicasNode.forEach(n -> caracteristicas.add(n.asText()));
                            herramienta.setCaracteristicas(caracteristicas);
                        }

                        if (herramientaNode.has("incluye") && !herramientaNode.get("incluye").isNull()) {
                            herramienta.setIncluye(herramientaNode.get("incluye").asText());
                        }

                        if (herramientaNode.has("descuento") && !herramientaNode.get("descuento").isNull()) {
                            herramienta.setDescuento(herramientaNode.get("descuento").asInt());
                        }

                        // Asignar la categoría usando idCategoria
                        int idCategoria = herramientaNode.get("idCategoria").asInt();
                        Categoria categoria = categoriasMap.get(idCategoria);
                        if (categoria != null) {
                            herramienta.setCategoria(categoria);
                            herramientaRepository.save(herramienta);
                            log.info("Herramienta cargada: {} - Categoría: {} - Stock: {}",
                                    nombre, categoria.getNombre(), herramienta.getStock());
                        } else {
                            log.warn("No se encontró la categoría con ID {} para la herramienta: {}", idCategoria, nombre);
                        }
                    }
                }
            }

            // --- Usuarios ---
            JsonNode usuariosNode = rootNode.get("usuarios");
            if (usuariosNode != null && usuariosNode.isArray()) {
                for (JsonNode usuarioNode : usuariosNode) {
                    String email = usuarioNode.get("email").asText();
                    if (usuarioRepository.findByEmail(email).isEmpty()) {
                        Usuario usuario = new Usuario();
                        usuario.setNombre(usuarioNode.get("nombre").asText());
                        usuario.setEmail(email);
                        // Cifrar contraseña
                        usuario.setContraseña(passwordEncoder.encode(usuarioNode.get("contraseña").asText()));
                        usuario.setNumeroTelefono(usuarioNode.get("numeroTelefonico").asText());
                        String rolStr = usuarioNode.get("rol").asText();
                        usuario.setRol(UsuarioRol.valueOf(rolStr));

                        // Dirección
                        JsonNode dir = usuarioNode.get("direccion");
                        Direccion direccion = new Direccion();
                        direccion.setCalle(dir.get("calle").asText());
                        direccion.setCiudad(dir.get("ciudad").asText());
                        direccion.setEstado(dir.get("estado").asText());
                        direccion.setCodigoPostal(dir.get("codigoPostal").asText());
                        direccion.setPais(dir.get("pais").asText());
                        usuario.setDireccion(direccion);

                        usuarioRepository.save(usuario);
                        log.info("Usuario cargado: {} - Rol: {}", usuario.getNombre(), usuario.getRol());
                    }
                }
            }

            log.info("Datos iniciales cargados exitosamente.");
            log.info("Total categorías: {}", categoriaRepository.count());
            log.info("Total herramientas: {}", herramientaRepository.count());

        } catch (IOException e) {
            log.error("Error al cargar datos iniciales desde JSON", e);
        }
    }
}
