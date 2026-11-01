# Diagrama de Arquitectura — Componentes

```mermaid
flowchart LR
  Navegador["🖥️ Navegador<br/>Cliente"] -->|HTTP| Frontend["Frontend<br/>Vue 3 + Vite"]
  Frontend -->|Axios / REST + JWT| Backend["Backend<br/>Spring Boot + JWT"]
  Backend -->|JPA / Hibernate| DB[("PostgreSQL<br/>Base de datos")]
  Backend -->|crea preferencia / consulta pago| MP["Mercado Pago<br/>API externa"]
  MP -->|webhook de pago · HTTPS directo| Backend
```

*El backend expone la API REST que consume el frontend; se comunica con Mercado Pago para crear pagos y recibe sus notificaciones por webhook, directo sobre HTTPS gracias al dominio público de Render (sin túneles).*
