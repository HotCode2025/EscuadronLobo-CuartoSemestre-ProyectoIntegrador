<template>
  <div class="pagina-info">
    <h1><Icono name="caja" /> Envíos y Entregas</h1>
    <section class="bloque">
      <h2>Cobertura</h2>
      <p>Realizamos envíos a todo el país a través de empresas de logística asociadas.</p>
    </section>
    <section class="bloque">
      <h2>Tiempos estimados</h2>
      <p>El tiempo de entrega estimado es de 3 a 7 días hábiles, dependiendo de la localidad de destino.</p>
    </section>
    <section class="bloque">
      <h2>Costos</h2>
      <p>El costo de envío se calcula según el peso del producto y la distancia, y se informa antes de confirmar la compra.</p>
    </section>
    <section class="bloque">
      <h2>Seguimiento</h2>
      <p>Podés seguir el estado de tu pedido desde la sección "Mis Compras" en cualquier momento.</p>
    </section>
    <router-link to="/" class="volver">← Volver al catálogo</router-link>
  </div>
</template>

<script setup>
import Icono from '../components/Icono.vue'
</script>

<style scoped>
.pagina-info {
  max-width: 720px;
  margin: 16px auto 40px;
  padding: 30px;
  background: var(--wf-bg);
  font-family: var(--wf-font-body);
  color: var(--wf-text);
}
.pagina-info h1 {
  text-align: center;
  font-size: 1.8em;
  margin-bottom: 30px;
}
.bloque {
  background: var(--wf-surface);
  border: 1px solid var(--wf-border);
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 16px;
}
.bloque h2 {
  font-size: 1.1em;
  color: var(--wf-accent);
  margin: 0 0 8px;
}
.bloque p {
  margin: 0;
  color: var(--wf-text-muted);
  line-height: 1.6;
}
.volver {
  display: inline-block;
  margin-top: 10px;
  color: var(--wf-accent);
  text-decoration: none;
  font-weight: 600;
}
.volver:hover {
  text-decoration: underline;
}
</style>
