<template>
  <nav class="navbar">
    <router-link to="/" class="brand">
      <img src="/branding/logo.png" alt="WolfForge" class="brand-logo" />
    </router-link>

    <div class="nav-links">
      <router-link to="/" class="nav-link">Catálogo</router-link>

      <div class="carrito-wrap" ref="carritoWrapRef">
        <button type="button" class="nav-link btn-carrito" @click="mostrarPreview = !mostrarPreview">
          <Icono name="carrito" class="icono-carrito" />
          <span class="texto-carrito">Carrito ({{ carritoStore.cantidadTotal }})</span>
        </button>

        <div v-if="mostrarPreview" class="carrito-preview">
          <div v-if="carritoStore.items.length === 0" class="preview-vacio">
            El carrito está vacío.
          </div>

          <template v-else>
            <div class="preview-lista">
              <div v-for="item in carritoStore.items" :key="item.idHerramienta" class="preview-item">
                <img v-if="item.imagenUrl" :src="item.imagenUrl" :alt="item.nombre" />
                <div v-else class="preview-sin-imagen"><Icono name="llave" /></div>
                <div class="preview-info">
                  <p class="preview-nombre">{{ item.nombre }}</p>
                  <p class="preview-cantidad">{{ item.cantidad }} × ${{ formatearPrecio(item.precio) }}</p>
                </div>
              </div>
            </div>
            <div class="preview-total">
              <span>Total</span>
              <strong>${{ formatearPrecio(carritoStore.total) }}</strong>
            </div>
            <router-link to="/carrito" class="preview-btn" @click="mostrarPreview = false">
              Ver carrito completo
            </router-link>
          </template>
        </div>
      </div>

      <router-link v-if="!authStore.isAuthenticated" to="/login" class="nav-link">
        Ingresar
      </router-link>

      <template v-else>
        <router-link to="/mis-compras" class="nav-link">Mis Compras</router-link>
        <router-link v-if="authStore.isAdmin" to="/admin" class="nav-link">Admin</router-link>
        <button @click="authStore.logout()" class="btn-salir">Salir</button>
      </template>

      <button
        v-if="route.path === '/'"
        type="button"
        class="btn-menu-filtros"
        @click="filtrosStore.abrir()"
        aria-label="Categorías y marcas"
      >
        <Icono name="menu" />
      </button>
    </div>
  </nav>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRoute } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import { useCarritoStore } from '../stores/carrito'
import { useFiltrosStore } from '../stores/filtros'
import Icono from './Icono.vue'
import { formatearPrecio } from '../utils/formato'

const route = useRoute()
const authStore = useAuthStore()
const carritoStore = useCarritoStore()
const filtrosStore = useFiltrosStore()

const mostrarPreview = ref(false)
const carritoWrapRef = ref(null)

function manejarClickAfuera(event) {
  if (carritoWrapRef.value && !carritoWrapRef.value.contains(event.target)) {
    mostrarPreview.value = false
  }
}

onMounted(() => document.addEventListener('click', manejarClickAfuera))
onUnmounted(() => document.removeEventListener('click', manejarClickAfuera))
</script>

<style scoped>
.navbar {
  background: var(--wf-bg-deep);
  padding: 14px 28px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 14px;
  border-bottom: 3px solid var(--wf-accent);
  box-shadow: 0 3px 10px rgba(0, 0, 0, 0.3);
  flex-wrap: wrap;
}

.brand {
  display: flex;
  align-items: center;
}

.brand-logo {
  height: 70px;
  width: auto;
}

.nav-links {
  display: flex;
  align-items: center;
  gap: 10px;
  flex-wrap: wrap;
}

.nav-link {
  color: var(--wf-text-muted);
  text-decoration: none;
  font-weight: 600;
  padding: 8px 14px;
  border-radius: 8px;
  transition: all 0.2s ease-in-out;
}

.nav-link:hover {
  background: var(--wf-surface);
  color: var(--wf-text);
}

.router-link-exact-active.nav-link {
  color: var(--wf-accent);
}

.btn-salir {
  background: var(--wf-accent-2);
  border: none;
  color: #fff;
  font-weight: 700;
  padding: 8px 16px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s ease-in-out;
}

.btn-salir:hover {
  background: var(--wf-accent-2-hover);
}

.carrito-wrap {
  position: relative;
}

.btn-carrito {
  background: none;
  border: none;
  font-family: inherit;
  font-size: inherit;
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  gap: 8px;
}

.icono-carrito {
  width: 20px;
  height: 20px;
  flex: none;
}

.carrito-preview {
  position: absolute;
  top: calc(100% + 8px);
  right: 0;
  width: 300px;
  max-width: calc(100vw - 32px);
  background: var(--wf-surface);
  border: 1px solid var(--wf-border);
  border-radius: 12px;
  box-shadow: 0 10px 28px rgba(0, 0, 0, 0.4);
  padding: 16px;
  z-index: 50;
}

.btn-menu-filtros {
  display: none;
  align-items: center;
  justify-content: center;
  background: var(--wf-surface);
  color: var(--wf-text);
  border: 1px solid var(--wf-border);
  border-radius: 8px;
  width: 36px;
  height: 36px;
  cursor: pointer;
}

@media (max-width: 600px) {
  .navbar {
    padding: 10px 14px;
    justify-content: center;
  }

  .brand-logo {
    height: 48px;
  }

  .nav-links {
    justify-content: center;
    gap: 6px;
  }

  .nav-link {
    padding: 6px 10px;
    font-size: 0.9em;
  }

  .texto-carrito {
    display: none;
  }

  .carrito-preview {
    right: auto;
    left: 50%;
    transform: translateX(-50%);
    width: min(300px, calc(100vw - 32px));
  }

  .btn-menu-filtros {
    display: flex;
  }
}

.preview-vacio {
  color: var(--wf-text-muted);
  text-align: center;
  padding: 10px 0;
  font-size: 0.9em;
}

.preview-lista {
  max-height: 280px;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-bottom: 12px;
}

.preview-item {
  display: flex;
  gap: 10px;
  align-items: center;
}

.preview-item img,
.preview-sin-imagen {
  width: 48px;
  height: 48px;
  object-fit: contain;
  background: #fff;
  border-radius: 8px;
  flex: none;
}

.preview-sin-imagen {
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--wf-surface-2);
}

.preview-info {
  flex: 1;
  min-width: 0;
}

.preview-nombre {
  margin: 0;
  color: var(--wf-text);
  font-size: 0.85em;
  font-weight: 600;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.preview-cantidad {
  margin: 2px 0 0;
  color: var(--wf-text-muted);
  font-size: 0.8em;
}

.preview-total {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 10px;
  border-top: 1px solid var(--wf-border);
  color: var(--wf-text);
  margin-bottom: 12px;
}
.preview-total strong {
  color: var(--wf-accent);
  font-size: 1.1em;
}

.preview-btn {
  display: block;
  text-align: center;
  background: var(--wf-accent);
  color: #161616;
  font-weight: 700;
  padding: 10px;
  border-radius: 8px;
  text-decoration: none;
}
.preview-btn:hover {
  background: var(--wf-accent-hover);
}
</style>
