package ar.com.wolfforge.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    @Value("${frontend.url:http://localhost:3000}")
    private String frontendUrl;

    // Único bean de CORS: Spring Security lo detecta automáticamente a través
    // de .cors(cors -> {}) en SecurityConfig, sin necesidad de un CorsFilter aparte.
    @Bean
    public CorsConfigurationSource corsConfigurationSource(Environment env) {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);

        List<String> allowedOrigins = new ArrayList<>();
        allowedOrigins.add(frontendUrl);

        // Los orígenes de localhost solo se habilitan fuera del profile de producción
        if (!env.acceptsProfiles(Profiles.of("prod"))) {
            allowedOrigins.addAll(Arrays.asList(
                    "http://localhost:3000",
                    "http://localhost:3001",
                    "http://localhost:5173",
                    "http://localhost:8080"
            ));
        }

        allowedOrigins.forEach(config::addAllowedOrigin);

        config.setAllowedHeaders(List.of("*"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
