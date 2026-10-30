<template>
  <div class="notfound-container">
    <Icono name="tuerca" class="notfound-icono" />
    <h1>404</h1>
    <p>No encontramos esta página.</p>
    <router-link to="/" class="btn-volver">← Volver al catálogo</router-link>
  </div>
</template>

<script setup>
import Icono from '../components/Icono.vue'
</script>

<style scoped>
.notfound-container {
  min-height: 70vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
  padding: 40px 20px;
  background: var(--wf-bg);
  color: var(--wf-text);
  font-family: var(--wf-font-body);
}

.notfound-icono {
  font-size: 3em;
  margin-bottom: 10px;
}

.notfound-container h1 {
  font-family: var(--wf-font-display);
  font-size: 4em;
  margin: 0;
  color: var(--wf-accent);
}

.notfound-container p {
  color: var(--wf-text-muted);
  margin: 10px 0 30px;
  font-size: 1.1em;
}

.btn-volver {
  background: var(--wf-accent);
  color: #161616;
  padding: 12px 24px;
  border-radius: 10px;
  text-decoration: none;
  font-weight: 700;
  transition: all 0.2s;
}

.btn-volver:hover {
  background: var(--wf-accent-hover);
}
</style>
