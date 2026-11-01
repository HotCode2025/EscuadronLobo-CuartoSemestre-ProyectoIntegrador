# Diagrama de Secuencia — Webhook de Mercado Pago

```mermaid
sequenceDiagram
  participant MP as Mercado Pago
  participant Backend as Backend (WebhookController)
  participant MPApi as Mercado Pago (API consulta)
  participant BD as BD (PostgreSQL)

  MP->>Backend: POST /webhooks/mercadopago/notificacion
  Backend->>Backend: valida firma x-signature (HMAC-SHA256)
  alt firma inválida (evento "Pagos legacy")
    Backend->>Backend: registra warning, continúa igual
  end
  Backend->>MPApi: GET /v1/payments/{id}
  MPApi-->>Backend: datos del pago real
  Backend->>BD: actualiza Pago y HerramientaPedido
  Backend-->>MP: 200 OK { received: true }
```

*Aunque falle la firma (caso conocido de eventos legacy) nunca se confía ciegamente: siempre se reconsulta el pago contra la API de Mercado Pago antes de actualizar la base.*
