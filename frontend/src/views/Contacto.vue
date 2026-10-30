<template>
  <div class="pagina-info">
    <h1><Icono name="caja" /> Contacto</h1>
    <section class="bloque">
      <h2>¿Tenés una consulta?</h2>
      <p>Escribinos y te respondemos a la brevedad:</p>
      <p class="dato"><Icono name="etiqueta" /> wolfforge.soporte@gmail.com</p>
      <p class="dato"><Icono name="reloj" /> Lunes a viernes de 9 a 18 hs</p>
    </section>
    <section class="bloque">
      <h2>¿Ya hiciste una compra?</h2>
      <p>
        Podés revisar el estado de tus pedidos desde
        <router-link to="/mis-compras" class="link-activo">Mis Compras</router-link>.
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
  margin: 6px 0;
  color: var(--wf-text-muted);
  line-height: 1.6;
}
.dato {
  display: flex;
  align-items: center;
  gap: 8px;
  color: var(--wf-text) !important;
  font-weight: 600;
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
