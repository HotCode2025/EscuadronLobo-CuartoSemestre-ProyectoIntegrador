<template>
  <div class="detalle-container">
    <a href="#" class="volver" @click.prevent="volverAlCatalogo">← Volver al catálogo</a>

    <div v-if="cargando" class="estado-mensaje">
      <div class="spinner"></div>
    </div>
    <div v-else-if="error" class="estado-mensaje">No se pudo cargar el producto.</div>

    <div v-else class="detalle-grid">
      <!-- Galería -->
      <div class="galeria">
        <div class="imagen-principal">
          <button
            v-if="miniaturas.length > 1"
            class="flecha flecha-izq"
            type="button"
            aria-label="Imagen anterior"
            @click="anterior"
          >‹</button>

          <img
            v-if="imagenActiva"
            :src="imagenActiva"
            :alt="herramienta.nombre"
            @error="manejarErrorImagen"
          />
          <div v-else class="sin-imagen"><Icono name="llave" /> Sin imagen</div>

          <button
            v-if="miniaturas.length > 1"
            class="flecha flecha-der"
            type="button"
            aria-label="Imagen siguiente"
            @click="siguiente"
          >›</button>
        </div>

        <div v-if="miniaturas.length > 1" class="miniaturas">
          <img
            v-for="(img, i) in miniaturas"
            :key="i"
            :src="img"
            :class="{ activa: img === imagenActiva }"
            @click="imagenActiva = img"
            @error="($event) => ($event.target.style.visibility = 'hidden')"
          />
        </div>
      </div>

      <!-- Info -->
      <div class="info">
        <p v-if="herramienta.categoria" class="categoria-tag">{{ herramienta.categoria.nombre }}</p>
        <h1>{{ herramienta.nombre }}</h1>
        <p class="marca">{{ herramienta.marca }}</p>
        <p class="precio">${{ formatearPrecio(herramienta.precio) }}</p>
        <p class="stock" :class="{ agotado: herramienta.stock === 0 }">
          {{ herramienta.stock > 0 ? `${herramienta.stock} disponibles` : 'Sin stock' }}
        </p>

        <div class="cantidad-selector">
          <button :disabled="cantidad <= 1" @click="cantidad = Math.max(1, cantidad - 1)">−</button>
          <span>{{ cantidad }}</span>
          <button :disabled="cantidad >= herramienta.stock" @click="cantidad = Math.min(herramienta.stock, cantidad + 1)">+</button>
        </div>

        <button
          class="btn-agregar"
          :disabled="herramienta.stock === 0"
          @click="agregarAlCarrito"
        >
          <Icono name="carrito" /> Agregar al Carrito
        </button>

        <p v-if="herramienta.descripcion" class="descripcion">{{ herramienta.descripcion }}</p>

        <div v-if="herramienta.caracteristicas?.length" class="seccion">
          <h3>Características técnicas</h3>
          <ul>
            <li v-for="(c, i) in herramienta.caracteristicas" :key="i">{{ c }}</li>
          </ul>
        </div>

        <div v-if="herramienta.incluye" class="seccion">
          <h3>Incluye</h3>
          <p>{{ herramienta.incluye }}</p>
        </div>
      </div>
    </div>

    <!-- Vistos recientemente -->
    <section v-if="vistosFiltrados.length > 0" class="vistos-seccion">
      <h2><Icono name="ojo" /> Vistos recientemente</h2>
      <div class="vistos-fila">
        <router-link
          v-for="visto in vistosFiltrados"
          :key="visto.idHerramienta"
          :to="`/herramienta/${visto.idHerramienta}`"
          class="visto-card"
        >
          <img v-if="visto.imagenUrl" :src="visto.imagenUrl" :alt="visto.nombre" />
          <div v-else class="sin-imagen-chica"><Icono name="llave" /></div>
          <h4>{{ visto.nombre }}</h4>
          <p>${{ formatearPrecio(visto.precio) }}</p>
        </router-link>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import api from '../services/api'
import { useCarritoStore } from '../stores/carrito'
import { useToastStore } from '../stores/toast'
import { vistosRecientemente, registrarVisto } from '../composables/vistosRecientemente'
import Icono from '../components/Icono.vue'
import { formatearPrecio } from '../utils/formato'

const route = useRoute()
const router = useRouter()
const carritoStore = useCarritoStore()
const toastStore = useToastStore()

function volverAlCatalogo() {
  if (window.history.state?.back) {
    router.back()
  } else {
    router.push('/')
  }
}

const herramienta = ref({})
const cargando = ref(true)
const error = ref(false)
const cantidad = ref(1)
const imagenActiva = ref(null)

const miniaturas = computed(() => {
  const lista = []
  if (herramienta.value.imagenUrl) lista.push(herramienta.value.imagenUrl)
  if (herramienta.value.imagenesSecundarias) lista.push(...herramienta.value.imagenesSecundarias)
  return lista
})

const vistosFiltrados = computed(() => {
  return vistosRecientemente.value.filter(v => v.idHerramienta !== herramienta.value.idHerramienta)
})

function moverImagen(delta) {
  const lista = miniaturas.value
  if (lista.length < 2) return
  const actual = lista.indexOf(imagenActiva.value)
  const siguienteIndice = (actual + delta + lista.length) % lista.length
  imagenActiva.value = lista[siguienteIndice]
}

function anterior() {
  moverImagen(-1)
}

function siguiente() {
  moverImagen(1)
}

async function cargar() {
  cargando.value = true
  error.value = false
  try {
    const { data } = await api.get(`/herramientas/obtener/${route.params.id}`)
    herramienta.value = data
    cantidad.value = 1
    imagenActiva.value = data.imagenUrl || null
    document.title = `${data.nombre} · WolfForge`
    registrarVisto(data)
  } catch (err) {
    console.error('Error al cargar la herramienta:', err)
    error.value = true
  } finally {
    cargando.value = false
  }
}

onMounted(cargar)
watch(() => route.params.id, cargar)

function agregarAlCarrito() {
  carritoStore.agregar(herramienta.value, cantidad.value)
  toastStore.exito('Herramienta agregada al carrito')
}

function manejarErrorImagen(event) {
  event.target.style.display = 'none'
}
</script>

<style scoped>
.detalle-container {
  max-width: 1100px;
  margin: 40px auto;
  padding: 0 20px 60px;
  font-family: var(--wf-font-body);
  color: var(--wf-text);
  background: var(--wf-bg);
}

.vistos-seccion {
  max-width: 1100px;
  margin: 0 auto 60px;
  padding: 0 20px;
}

.vistos-seccion h2 {
  color: var(--wf-text);
  font-size: 1.2em;
  margin-bottom: 16px;
}

.vistos-fila {
  display: flex;
  gap: 16px;
  overflow-x: auto;
  min-width: 0;
  padding-bottom: 8px;
}

.visto-card {
  flex: 0 0 150px;
  background: var(--wf-surface);
  border: 1px solid var(--wf-border);
  border-radius: 12px;
  padding: 12px;
  text-decoration: none;
  text-align: center;
  transition: border-color 0.2s, transform 0.2s;
}
.visto-card:hover {
  border-color: var(--wf-accent);
  transform: translateY(-3px);
}

.visto-card img,
.sin-imagen-chica {
  width: 100%;
  height: 90px;
  object-fit: contain;
  background: #fff;
  border-radius: 8px;
  margin-bottom: 8px;
}

.sin-imagen-chica {
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--wf-surface-2);
  font-size: 1.6em;
}

.visto-card h4 {
  margin: 0 0 4px;
  color: var(--wf-text);
  font-size: 0.82em;
  min-height: 2.2em;
  overflow: hidden;
}

.visto-card p {
  margin: 0;
  color: var(--wf-accent);
  font-weight: 700;
  font-size: 0.9em;
}

.volver {
  display: inline-block;
  margin-bottom: 24px;
  color: var(--wf-accent);
  font-weight: 600;
  text-decoration: none;
}
.volver:hover {
  text-decoration: underline;
}

.estado-mensaje {
  text-align: center;
  padding: 80px 20px;
  color: var(--wf-text-muted);
  font-size: 1.2em;
}

.spinner {
  width: 48px;
  height: 48px;
  margin: 0 auto;
  border: 4px solid var(--wf-border);
  border-top: 4px solid var(--wf-accent);
  border-radius: 50%;
  animation: girar 0.8s linear infinite;
}

@keyframes girar {
  to { transform: rotate(360deg); }
}

.detalle-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 40px;
}

@media (max-width: 768px) {
  .detalle-grid {
    grid-template-columns: 1fr;
  }

  .detalle-container {
    margin: 24px auto;
    padding: 0 16px 40px;
  }

  .imagen-principal {
    height: 300px;
  }
}

@media (max-width: 480px) {
  .detalle-container {
    margin: 16px auto;
    padding: 0 12px 32px;
  }

  .imagen-principal {
    height: 240px;
    border-radius: 12px;
  }

  .flecha {
    width: 34px;
    height: 34px;
    font-size: 1.3em;
  }

  .miniaturas img {
    width: 52px;
    height: 52px;
  }

  .info h1 {
    font-size: 1.4em;
  }

  .precio {
    font-size: 1.5em;
  }

  .btn-agregar {
    padding: 12px;
    font-size: 1em;
  }

  .vistos-seccion {
    padding: 0 12px;
  }

  .visto-card {
    flex: 0 0 120px;
  }
}

/* Galería */
.imagen-principal {
  position: relative;
  background: #fff;
  border-radius: 16px;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 380px;
}

.imagen-principal img {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
}

.flecha {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  width: 40px;
  height: 40px;
  border-radius: 50%;
  border: none;
  background: var(--wf-bg-deep);
  color: var(--wf-text);
  font-size: 1.6em;
  line-height: 1;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0.85;
  transition: all 0.2s;
  z-index: 2;
}

.flecha:hover {
  opacity: 1;
  background: var(--wf-accent);
  color: #161616;
}

.flecha-izq {
  left: 10px;
}

.flecha-der {
  right: 10px;
}

.sin-imagen {
  color: var(--wf-text-muted);
  font-size: 1.2em;
}

.miniaturas {
  display: flex;
  gap: 10px;
  margin-top: 12px;
  flex-wrap: wrap;
}

.miniaturas img {
  width: 64px;
  height: 64px;
  object-fit: contain;
  background: #fff;
  border-radius: 8px;
  border: 2px solid var(--wf-border);
  cursor: pointer;
  padding: 4px;
  box-sizing: border-box;
}

.miniaturas img.activa {
  border-color: var(--wf-accent);
}

/* Info */
.categoria-tag {
  display: inline-block;
  background: var(--wf-surface-2);
  color: var(--wf-text-muted);
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 0.85em;
  font-weight: 600;
  margin-bottom: 10px;
}

.info h1 {
  font-size: 1.8em;
  margin: 0 0 6px;
  color: var(--wf-text);
}

.marca {
  color: var(--wf-text-muted);
  font-style: italic;
  margin: 0 0 14px;
}

.precio {
  font-size: 1.8em;
  font-weight: bold;
  color: var(--wf-accent);
  margin: 0 0 6px;
}

.stock {
  color: var(--wf-success);
  font-weight: 600;
  margin-bottom: 18px;
}
.stock.agotado {
  color: var(--wf-accent-2);
}

.cantidad-selector {
  display: flex;
  align-items: center;
  gap: 14px;
  margin-bottom: 16px;
}
.cantidad-selector button {
  width: 36px;
  height: 36px;
  border-radius: 8px;
  border: none;
  background: var(--wf-surface-2);
  color: var(--wf-text);
  font-size: 18px;
  font-weight: bold;
  cursor: pointer;
  transition: background 0.15s, transform 0.1s;
}
.cantidad-selector button:hover:not(:disabled) {
  background: var(--wf-accent);
  color: #161616;
}
.cantidad-selector button:active:not(:disabled) {
  transform: scale(0.92);
}
.cantidad-selector button:disabled {
  opacity: 0.4;
  cursor: not-allowed;
}
.cantidad-selector span {
  min-width: 24px;
  text-align: center;
  font-weight: 600;
}

.btn-agregar {
  width: 100%;
  padding: 14px;
  background: var(--wf-accent);
  color: #161616;
  border: none;
  border-radius: 10px;
  font-size: 1.05em;
  font-weight: 700;
  cursor: pointer;
  transition: 0.3s;
  margin-bottom: 24px;
}
.btn-agregar:hover:not(:disabled) {
  background: var(--wf-accent-hover);
  transform: scale(1.02);
}
.btn-agregar:disabled {
  background: var(--wf-border);
  color: var(--wf-text-muted);
  cursor: not-allowed;
}

.descripcion {
  line-height: 1.6;
  color: var(--wf-text-muted);
  margin-bottom: 24px;
}

.seccion {
  margin-top: 24px;
  border-top: 1px solid var(--wf-border);
  padding-top: 18px;
}
.seccion h3 {
  margin: 0 0 10px;
  color: var(--wf-text);
  font-size: 1.05em;
}
.seccion ul {
  margin: 0;
  padding-left: 20px;
}
.seccion li {
  margin-bottom: 6px;
  line-height: 1.5;
}
.seccion p {
  line-height: 1.6;
  margin: 0;
}
</style>
