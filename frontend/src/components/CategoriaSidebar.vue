<template>
  <nav class="sidebar" aria-label="Filtros de catálogo">
    <div class="overlay" v-if="filtrosStore.abierto" @click="filtrosStore.cerrar()"></div>

    <div class="contenido-filtros" :class="{ abierto: filtrosStore.abierto }">
    <div class="drawer-header">
      <h3 class="sidebar-titulo">Menú</h3>
      <button type="button" class="btn-cerrar" @click="filtrosStore.cerrar()" aria-label="Cerrar">
        <Icono name="x-circulo" />
      </button>
    </div>

    <h3 class="sidebar-titulo">Categorías</h3>

    <div class="sidebar-scroll">
      <!-- Opción de todas las categorías -->
      <button
        type="button"
        class="sidebar-item"
        :class="{ activo: categoriaSeleccionada === null }"
        :aria-pressed="categoriaSeleccionada === null"
        @click="elegirCategoria(null)"
      >
        <Icono name="carpeta" /> Todas las categorías
      </button>

      <!-- Lista de categorías -->
      <button
        type="button"
        v-for="categoria in categorias"
        :key="categoria.idCategoria"
        class="sidebar-item"
        :class="{ activo: categoriaSeleccionada === categoria.idCategoria }"
        :aria-pressed="categoriaSeleccionada === categoria.idCategoria"
        @click="elegirCategoria(categoria.idCategoria)"
      >
        {{ categoria.nombre }}
      </button>
    </div>

    <!-- Filtro de marcas -->
    <template v-if="marcas && marcas.length">
      <h3 class="sidebar-titulo sidebar-titulo-marcas"><Icono name="etiqueta" /> Marcas</h3>

      <div class="sidebar-scroll">
        <button
          type="button"
          class="sidebar-item"
          :class="{ activo: marcaSeleccionada === null }"
          :aria-pressed="marcaSeleccionada === null"
          @click="elegirMarca(null)"
        >
          <Icono name="carpeta" /> Todas las marcas
        </button>

        <button
          type="button"
          v-for="marca in marcas"
          :key="marca"
          class="sidebar-item"
          :class="{ activo: marcaSeleccionada === marca }"
          :aria-pressed="marcaSeleccionada === marca"
          @click="elegirMarca(marca)"
        >
          {{ marca }}
        </button>
      </div>
    </template>
    </div>
  </nav>
</template>

<script setup>
import Icono from './Icono.vue'
import { useFiltrosStore } from '../stores/filtros'

const filtrosStore = useFiltrosStore()

defineProps({
  categorias: Array,
  categoriaSeleccionada: Number,
  marcas: Array,
  marcaSeleccionada: String
})
const emit = defineEmits(['seleccionar', 'seleccionarMarca'])

function elegirCategoria(id) {
  emit('seleccionar', id)
  filtrosStore.cerrar()
}

function elegirMarca(marca) {
  emit('seleccionarMarca', marca)
  filtrosStore.cerrar()
}
</script>

<style scoped>
.drawer-header {
  display: none;
}

.sidebar {
  width: 220px;
  flex: none;
  background: var(--wf-bg-deep);
  border-right: 3px solid var(--wf-border);
  padding: 20px;
  min-height: 100vh;
  font-family: var(--wf-font-body);
  box-sizing: border-box;
}

.sidebar-titulo {
  margin: 0 0 20px;
  color: var(--wf-text-muted);
  text-align: center;
  font-weight: bold;
  font-size: 1.1em;
  text-transform: uppercase;
  letter-spacing: 0.08em;
}

.sidebar-titulo-marcas {
  margin-top: 28px;
}

.sidebar-item {
  display: block;
  width: 100%;
  border: none;
  font-family: inherit;
  font-size: 1em;
  padding: 10px;
  margin-bottom: 8px;
  cursor: pointer;
  text-align: center;
  background-color: var(--wf-surface);
  color: var(--wf-text-muted);
  border-radius: 10px;
  font-weight: normal;
  transition: all 0.2s ease-in-out;
}

.sidebar-item:hover {
  background-color: var(--wf-surface-2);
  color: var(--wf-text);
}

.sidebar-item:focus-visible {
  outline: 2px solid var(--wf-accent);
  outline-offset: 2px;
}

.sidebar-item.activo {
  background-color: var(--wf-accent);
  color: #161616;
  font-weight: bold;
}

/* En mobile, el sidebar pasa a ser un botón que despliega un drawer */
@media (max-width: 768px) {
  .sidebar {
    width: auto;
    min-height: 0;
    border-right: none;
    border-bottom: none;
    padding: 0;
  }

  .overlay {
    position: fixed;
    inset: 0;
    background: rgba(0, 0, 0, 0.6);
    z-index: 300;
  }

  .contenido-filtros {
    position: fixed;
    top: 0;
    right: 0;
    height: 100vh;
    width: min(280px, 85vw);
    background: var(--wf-bg-deep);
    border-left: 1px solid var(--wf-border);
    padding: 20px;
    box-sizing: border-box;
    overflow-y: auto;
    z-index: 301;
    transform: translateX(100%);
    transition: transform 0.25s ease;
  }

  .contenido-filtros.abierto {
    transform: translateX(0);
  }

  .drawer-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 20px;
  }

  .drawer-header .sidebar-titulo {
    margin: 0;
    color: var(--wf-accent);
  }

  .btn-cerrar {
    background: none;
    border: none;
    color: var(--wf-text-muted);
    cursor: pointer;
    display: flex;
    padding: 2px;
  }

  .btn-cerrar :deep(svg) {
    width: 24px;
    height: 24px;
  }
}
</style>
