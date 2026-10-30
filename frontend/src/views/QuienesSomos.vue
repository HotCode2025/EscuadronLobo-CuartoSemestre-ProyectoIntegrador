<template>
  <div class="pagina-info">
    <h1><Icono name="llave" /> Quiénes Somos</h1>
    <section class="bloque">
      <h2>Escuadrón Lobo</h2>
      <p>
        WolfForge es un proyecto integrador desarrollado por <strong>Escuadrón Lobo</strong>,
        un grupo de estudiantes de la Tecnicatura en Programación en la UTN Facultad Regional San Rafael.
      </p>
    </section>
    <section class="bloque">
      <h2>El proyecto</h2>
      <p>
        Armamos una tienda de herramientas de punta a punta: autenticación, catálogo,
        carrito, pagos reales con Mercado Pago y un panel de administración completo —
        aplicando lo aprendido durante la cursada de Programación IV sobre e-commerce.
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
