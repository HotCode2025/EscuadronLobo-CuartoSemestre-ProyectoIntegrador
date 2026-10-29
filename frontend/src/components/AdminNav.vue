<template>
  <div class="admin-nav">
    <router-link to="/admin" class="admin-nav-link" :class="{ activo: $route.path === '/admin' }">
      <Icono name="caja" /> Pedidos
    </router-link>
    <router-link to="/admin/herramientas" class="admin-nav-link" :class="{ activo: $route.path === '/admin/herramientas' }">
      <Icono name="llave" /> Herramientas
    </router-link>
  </div>
</template>

<script setup>
import Icono from './Icono.vue'
</script>

<style scoped>
.admin-nav {
  display: flex;
  gap: 8px;
  justify-content: center;
  margin-bottom: 24px;
}

.admin-nav-link {
  padding: 10px 20px;
  border-radius: 10px;
  text-decoration: none;
  color: var(--wf-text-muted);
  font-weight: 600;
  background: var(--wf-surface);
  border: 1px solid var(--wf-border);
  transition: all 0.2s ease-in-out;
}

.admin-nav-link:hover {
  color: var(--wf-text);
  background: var(--wf-surface-2);
}

.admin-nav-link.activo {
  color: #161616;
  background: var(--wf-accent);
  border-color: var(--wf-accent);
}
</style>
