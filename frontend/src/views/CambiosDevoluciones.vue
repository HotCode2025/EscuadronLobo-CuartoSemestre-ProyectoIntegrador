<template>
  <div class="pagina-info">
    <h1><Icono name="reintentar" /> Cambios y Devoluciones</h1>
    <section class="bloque">
      <h2>¿Cómo solicito una devolución?</h2>
      <p>
        Una vez que confirmaste la recepción del producto desde "Mis Compras", vas a ver
        disponible el botón "Solicitar devolución" en ese mismo pedido.
      </p>
    </section>
    <section class="bloque">
      <h2>¿Qué pasa después de pedirla?</h2>
      <p>
        La solicitud queda marcada como "en revisión" y nuestro equipo la evalúa. Si se
        aprueba, el producto se marca como devuelto y el stock se repone automáticamente.
      </p>
    </section>
    <section class="bloque">
      <h2>¿Puedo cancelar en vez de devolver?</h2>
      <p>
        Sí, pero solo mientras el pedido todavía no fue entregado — una vez entregado, la
        única opción es la devolución.
      </p>
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
