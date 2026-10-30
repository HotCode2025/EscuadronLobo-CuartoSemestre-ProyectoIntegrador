<template>
  <div class="pagina-info">
    <h1><Icono name="check-circulo" /> Garantías</h1>
    <section class="bloque">
      <h2>Garantía oficial</h2>
      <p>
        Todos los productos de WolfForge cuentan con la garantía oficial del fabricante
        contra defectos de fabricación.
      </p>
    </section>
    <section class="bloque">
      <h2>¿Qué cubre?</h2>
      <p>
        Fallas de funcionamiento no derivadas de un uso indebido. No cubre desgaste
        normal por uso ni daños ocasionados por mal uso del producto.
      </p>
    </section>
    <section class="bloque">
      <h2>¿Cómo la hago efectiva?</h2>
      <p>
        Contactanos desde la sección de <router-link to="/contacto" class="link-activo">Contacto</router-link>
        indicando el número de pedido y una descripción del problema.
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
.link-activo {
  color: var(--wf-accent);
  font-weight: 600;
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
