# Diagrama de Secuencia — Registro de Usuario

```mermaid
sequenceDiagram
  actor Usuario
  participant Frontend as Frontend (Vue)
  participant Backend as Backend (AutenticacionController)
  participant BD as BD (PostgreSQL)

  Usuario->>Frontend: completa formulario de registro
  Frontend->>Backend: POST /api/auth/register
  Backend->>Backend: valida datos (Bean Validation)
  Backend->>Backend: hashea contraseña (BCrypt)
  Backend->>BD: INSERT usuario
  Backend->>BD: INSERT direccion (cascade de Usuario.direccion)
  BD-->>Backend: usuario + dirección guardados
  Backend->>Backend: genera JWT
  Backend-->>Frontend: 200 OK { token, usuario }
  Frontend-->>Usuario: guarda token y redirige al inicio
```

*El usuario queda autenticado inmediatamente después de registrarse, sin pasar por login. La dirección del formulario se guarda sola junto con el usuario, por el `cascade = ALL` de `Usuario.direccion`.*
