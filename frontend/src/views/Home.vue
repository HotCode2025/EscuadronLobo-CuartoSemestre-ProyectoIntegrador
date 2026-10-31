<template>
  <div class="home-container">
    <!-- Sidebar -->
    <CategoriaSidebar
      :categorias="categorias"
      :categoriaSeleccionada="categoriaSeleccionada"
      :marcas="marcas"
      :marcaSeleccionada="marcaSeleccionada"
      @seleccionar="seleccionarCategoria"
      @seleccionarMarca="seleccionarMarca"
    />

    <!-- Contenido principal -->
    <div class="home-content">
      <section class="hero-banner">
        <div class="hero-texto">
          <h2>Herramientas profesionales<br />para cada trabajo</h2>
          <p>Bosch, DeWalt, Makita, Lüsqtoff y más — equipamiento real para el taller, la obra y el hogar.</p>
          <a href="#ofertas" class="hero-boton">Ver ofertas del día</a>
        </div>
      </section>

      <h1 class="nombre"><Icono name="llave" /> Catálogo de Herramientas</h1>

      <!-- Ofertas del día -->
      <section v-if="ofertas.length > 0" id="ofertas" class="ofertas-seccion">
        <h2 class="ofertas-titulo"><Icono name="fuego" /> Ofertas del Día</h2>
        <div class="ofertas-fila">
          <div
            v-for="oferta in ofertas"
            :key="oferta.idHerramienta"
            class="oferta-card"
            @click="$router.push(`/herramienta/${oferta.idHerramienta}`)"
          >
            <span class="oferta-badge">{{ oferta.descuento }}% OFF</span>
            <img
              v-if="oferta.imagenUrl"
              :src="oferta.imagenUrl"
              :alt="oferta.nombre"
              @error="manejarErrorImagen"
            />
            <div v-else class="sin-imagen"><Icono name="llave" /></div>
            <h3>{{ oferta.nombre }}</h3>
            <div class="oferta-precios">
              <span class="precio-viejo">${{ precioOriginal(oferta) }}</span>
              <span class="precio-nuevo">${{ formatearPrecio(oferta.precio) }}</span>
            </div>
            <button @click.stop="agregarAlCarrito(oferta)" class="btn-oferta" :disabled="oferta.stock === 0">
              <template v-if="oferta.stock === 0">Sin stock</template>
              <template v-else><Icono name="carrito" /> Agregar</template>
            </button>
          </div>
        </div>
      </section>

      <div class="buscador">
        <input
          v-model="busqueda"
          type="text"
          placeholder="Buscar por nombre o marca..."
          class="input-buscar"
        />
        <select v-model="orden" class="select-orden">
          <option value="relevancia">Relevancia</option>
          <option value="precio-asc">Precio: menor a mayor</option>
          <option value="precio-desc">Precio: mayor a menor</option>
          <option value="nombre-asc">Nombre (A-Z)</option>
        </select>
      </div>

      <p v-if="categoriaSeleccionada || marcaSeleccionada" class="categoria-subnombre">
        Mostrando:
        <strong v-if="categoriaNombre">{{ categoriaNombre }}</strong>
        <span v-if="categoriaNombre && marcaSeleccionada"> · </span>
        <strong v-if="marcaSeleccionada">{{ marcaSeleccionada }}</strong>
      </p>

      <!-- Cargando -->
      <div v-if="cargando" class="herramientas-grid">
        <div v-for="n in 8" :key="n" class="skeleton-card"></div>
      </div>

      <!-- Sin herramientas -->
      <div v-else-if="herramientasFiltrados.length === 0" class="sin-herramientas">
        <p>No hay herramientas que coincidan con la búsqueda...</p>
        <Icono name="tuerca" />
      </div>

      <!-- Grid de herramientas -->
      <div v-else class="herramientas-grid">
        <div
          v-for="herramienta in herramientasPaginados"
          :key="herramienta.idHerramienta"
          class="herramienta-card"
          @mouseover="hover = herramienta.idHerramienta"
          @mouseleave="hover = null"
          :class="{ hover: hover === herramienta.idHerramienta }"
        >
          <!-- Enlace al detalle -->
          <router-link :to="`/herramienta/${herramienta.idHerramienta}`" class="herramienta-link">
            <div class="imagen-wrap">
              <img
                v-if="herramienta.imagenUrl"
                :src="herramienta.imagenUrl"
                :alt="herramienta.nombre"
                @error="manejarErrorImagen"
                class="herramienta-imagen"
                loading="lazy"
              />
              <div v-else class="sin-imagen"><Icono name="llave" /> Sin imagen</div>
              <span v-if="herramienta.stock === 0" class="badge-agotado">Sin stock</span>
            </div>

            <h3 class="herramienta-nombre">{{ herramienta.nombre }}</h3>
            <p class="herramienta-marca">{{ herramienta.marca }}</p>
            <p class="herramienta-precio">${{ formatearPrecio(herramienta.precio) }}</p>
          </router-link>

          <!-- Botón -->
          <button
            @click="agregarAlCarrito(herramienta)"
            class="btn-agregar"
            :disabled="herramienta.stock === 0"
            @mouseover="botonHover = herramienta.idHerramienta"
            @mouseleave="botonHover = null"
            :class="{ hover: botonHover === herramienta.idHerramienta }"
          >
            <template v-if="herramienta.stock === 0">Sin stock</template>
            <template v-else><Icono name="carrito" /> Agregar al Carrito</template>
          </button>
        </div>
      </div>

      <div v-if="hayMasParaCargar" class="cargar-mas-wrap">
        <button class="btn-cargar-mas" @click="cargarMas">
          Cargar más ({{ herramientasFiltrados.length - herramientasPaginados.length }} restantes)
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import api from '../services/api'
import { useCarritoStore } from '../stores/carrito'
import { useToastStore } from '../stores/toast'
import CategoriaSidebar from '../components/CategoriaSidebar.vue'
import Icono from '../components/Icono.vue'
import { formatearPrecio } from '../utils/formato'

const route = useRoute()
const router = useRouter()

const herramientas = ref([])
const categorias = ref([])
const ofertas = ref([])
const categoriaSeleccionada = ref(route.query.categoria ? Number(route.query.categoria) : null)
const marcaSeleccionada = ref(route.query.marca || null)
const busqueda = ref(route.query.q || '')
const orden = ref(route.query.orden || 'relevancia')
const cargando = ref(true)
const POR_PAGINA = 24
const paginaActual = ref(1)
const carritoStore = useCarritoStore()
const toastStore = useToastStore()
const hover = ref(null)
const botonHover = ref(null)

const marcas = computed(() => {
  const unicas = new Set(herramientas.value.map(h => h.marca).filter(Boolean))
  return [...unicas].sort()
})

const herramientasFiltrados = computed(() => {
  const texto = busqueda.value.trim().toLowerCase()

  const filtrados = herramientas.value.filter(herramienta => {
    const coincideCategoria = categoriaSeleccionada.value === null ||
      herramienta.categoria?.idCategoria === categoriaSeleccionada.value
    const coincideMarca = marcaSeleccionada.value === null ||
      herramienta.marca === marcaSeleccionada.value
    const coincideTexto = texto === '' ||
      herramienta.nombre?.toLowerCase().includes(texto) ||
      herramienta.marca?.toLowerCase().includes(texto)
    return coincideCategoria && coincideMarca && coincideTexto
  })

  switch (orden.value) {
    case 'precio-asc':
      return [...filtrados].sort((a, b) => a.precio - b.precio)
    case 'precio-desc':
      return [...filtrados].sort((a, b) => b.precio - a.precio)
    case 'nombre-asc':
      return [...filtrados].sort((a, b) => a.nombre.localeCompare(b.nombre))
    default:
      return filtrados
  }
})

const categoriaNombre = computed(() => {
  const categoria = categorias.value.find(c => c.idCategoria === categoriaSeleccionada.value)
  return categoria?.nombre || ''
})

const herramientasPaginados = computed(() => {
  return herramientasFiltrados.value.slice(0, paginaActual.value * POR_PAGINA)
})

const hayMasParaCargar = computed(() => {
  return herramientasPaginados.value.length < herramientasFiltrados.value.length
})

function cargarMas() {
  paginaActual.value++
}

watch([busqueda, orden, categoriaSeleccionada, marcaSeleccionada], () => {
  paginaActual.value = 1
})

watch([categoriaSeleccionada, marcaSeleccionada, busqueda, orden], () => {
  const query = {}
  if (categoriaSeleccionada.value !== null) query.categoria = categoriaSeleccionada.value
  if (marcaSeleccionada.value !== null) query.marca = marcaSeleccionada.value
  if (busqueda.value.trim() !== '') query.q = busqueda.value.trim()
  if (orden.value !== 'relevancia') query.orden = orden.value
  router.replace({ query })
})

onMounted(async () => {
  try {
    const [herramientasRes, categoriasRes] = await Promise.all([
      api.get('/herramientas/listar'),
      api.get('/categorias/listar')
    ])
    herramientas.value = herramientasRes.data
    categorias.value = categoriasRes.data
  } catch (err) {
    console.error('Error al cargar datos:', err)
    toastStore.error('No se pudo cargar el catálogo')
  } finally {
    cargando.value = false
  }

  try {
    const { data } = await api.get('/herramientas/ofertas')
    ofertas.value = data
  } catch (err) {
    console.error('Error al cargar ofertas:', err)
  }
})

function precioOriginal(oferta) {
  const original = oferta.precio / (1 - oferta.descuento / 100)
  return formatearPrecio(original)
}

function seleccionarCategoria(idCategoria) {
  categoriaSeleccionada.value = idCategoria
}

function seleccionarMarca(marca) {
  marcaSeleccionada.value = marca
}

async function agregarAlCarrito(herramienta) {
  try {
    const { data: herramientaActual } = await api.get(`/herramientas/obtener/${herramienta.idHerramienta}`)
    const stockDisponible = herramientaActual.stock ?? 999
    const item = carritoStore.items.find(i => i.idHerramienta === herramienta.idHerramienta)
    const cantidadActual = item ? item.cantidad : 0

    if (cantidadActual >= stockDisponible) {
      toastStore.error(`No hay más stock disponible (${stockDisponible})`)
      return
    }

    carritoStore.agregar(herramienta)
    toastStore.exito('Herramienta agregada al carrito')
  } catch (err) {
    console.error('Error al agregar al carrito:', err)
    toastStore.error('Error al verificar stock')
  }
}

function manejarErrorImagen(event) {
  event.target.style.display = 'none'
  event.target.nextElementSibling.style.display = 'flex'
}
</script>

<style>
/* Ofertas del día */
.ofertas-seccion {
  max-width: 1200px;
  margin: 0 auto 30px;
}

.ofertas-titulo {
  text-align: center;
  color: var(--wf-text);
  font-size: 1.3em;
  margin-bottom: 16px;
}

.ofertas-fila {
  display: flex;
  gap: 18px;
  overflow-x: auto;
  padding-top: 8px;
  padding-bottom: 8px;
  width: 100%;
  min-width: 0;
  scrollbar-width: thin;
  scrollbar-color: var(--wf-accent) transparent;
}

.ofertas-fila::-webkit-scrollbar {
  height: 6px;
}

.ofertas-fila::-webkit-scrollbar-track {
  background: transparent;
}

.ofertas-fila::-webkit-scrollbar-thumb {
  background: var(--wf-accent);
  border-radius: 10px;
}

.ofertas-fila::-webkit-scrollbar-thumb:hover {
  background: var(--wf-accent-hover);
}

.oferta-card {
  position: relative;
  flex: 0 0 180px;
  background: var(--wf-surface);
  border: 1px solid var(--wf-accent);
  border-radius: 14px;
  padding: 14px;
  text-align: center;
  cursor: pointer;
  transition: transform 0.2s;
}
.oferta-card:hover {
  transform: translateY(-4px);
}

.oferta-badge {
  position: absolute;
  top: 10px;
  left: 10px;
  background: var(--wf-accent-2);
  color: #fff;
  font-size: 0.75em;
  font-weight: 700;
  padding: 3px 8px;
  border-radius: 20px;
}

.oferta-card img {
  width: 100%;
  height: 110px;
  object-fit: contain;
  background: #fff;
  border-radius: 10px;
  margin-bottom: 10px;
}

.oferta-card .sin-imagen {
  width: 100%;
  height: 110px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--wf-surface-2);
  border-radius: 10px;
  margin-bottom: 10px;
  font-size: 1.8em;
}

.oferta-card h3 {
  font-size: 0.85em;
  color: var(--wf-text);
  margin: 0 0 8px;
  min-height: 2.2em;
}

.oferta-precios {
  display: flex;
  flex-direction: column;
  margin-bottom: 10px;
}

.precio-viejo {
  color: var(--wf-text-muted);
  text-decoration: line-through;
  font-size: 0.85em;
}

.precio-nuevo {
  color: var(--wf-accent);
  font-weight: 700;
  font-size: 1.1em;
}

.btn-oferta {
  width: 100%;
  padding: 8px;
  background: var(--wf-accent);
  color: #161616;
  border: none;
  border-radius: 8px;
  font-weight: 700;
  cursor: pointer;
}
.btn-oferta:hover:not(:disabled) {
  background: var(--wf-accent-hover);
}
.btn-oferta:disabled {
  background: var(--wf-border);
  color: var(--wf-text-muted);
  cursor: not-allowed;
}

.home-container {
  display: flex;
  min-height: 100vh;
  background: var(--wf-bg);
  font-family: var(--wf-font-body);
  color: var(--wf-text);
  animation: fadeIn 0.6s ease;
}

@media (max-width: 768px) {
  .home-container {
    flex-direction: column;
  }

  .home-content {
    margin: 8px 12px;
    padding: 12px;
  }

  .nombre {
    font-size: 1.8em;
    margin-bottom: 14px;
  }
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(-10px); }
  to { opacity: 1; transform: translateY(0); }
}

.home-content {
  flex: 1;
  min-width: 0;
  padding: 20px 40px 40px;
  background: var(--wf-bg);
  animation: scaleIn 0.6s ease-out;
}

@keyframes scaleIn {
  from { transform: scale(0.95); opacity: 0; }
  to { transform: scale(1); opacity: 1; }
}

.hero-banner {
  display: flex;
  align-items: center;
  min-height: 280px;
  background:
    linear-gradient(to right, rgba(13, 13, 13, 0.82) 0%, rgba(13, 13, 13, 0.55) 40%, transparent 70%),
    url('/branding/hero-banner-limpio.jpg') 80% 35% / cover no-repeat;
  border-radius: 18px;
  padding: 36px 40px;
  margin-bottom: 30px;
  overflow: hidden;
}

.hero-texto {
  flex: 1;
  min-width: 260px;
  max-width: 460px;
  color: #F5F5F5;
}

.hero-texto h2 {
  font-size: 2em;
  line-height: 1.15;
  margin: 0 0 12px;
}

.hero-texto p {
  margin: 0 0 20px;
  font-size: 1.05em;
  max-width: 420px;
  opacity: 0.85;
}

.hero-boton {
  display: inline-block;
  background: var(--wf-accent);
  color: #161616;
  font-weight: 700;
  padding: 12px 24px;
  border-radius: 10px;
  text-decoration: none;
  transition: transform 0.2s ease;
}

.hero-boton:hover {
  transform: scale(1.04);
}

@media (max-width: 768px) {
  .hero-banner {
    min-height: 220px;
    padding: 28px 24px;
    text-align: center;
    justify-content: center;
    background-position: 85% 30%;
  }

  .hero-texto p {
    max-width: none;
  }
}

.nombre {
  font-size: 2.4em;
  margin-bottom: 20px;
  color: var(--wf-text);
  text-align: center;
}

.buscador {
  max-width: 620px;
  margin: 0 auto 20px;
  display: flex;
  gap: 12px;
}

@media (max-width: 600px) {
  .buscador {
    flex-direction: column;
  }
}

.input-buscar {
  flex: 1;
  padding: 12px 16px;
  border-radius: 10px;
  border: 1px solid var(--wf-border);
  background: var(--wf-surface);
  color: var(--wf-text);
  font-size: 1em;
  box-sizing: border-box;
}
.input-buscar:focus {
  outline: none;
  border-color: var(--wf-accent);
}

.select-orden {
  padding: 12px 16px;
  border-radius: 10px;
  border: 1px solid var(--wf-border);
  background: var(--wf-surface);
  color: var(--wf-text);
  font-size: 1em;
  box-sizing: border-box;
  cursor: pointer;
}
.select-orden:focus {
  outline: none;
  border-color: var(--wf-accent);
}

.categoria-subnombre {
  color: var(--wf-text-muted);
  text-align: center;
  margin-bottom: 25px;
  font-style: italic;
}

.categoria-subnombre strong {
  color: var(--wf-accent);
}

/* Sin herramientas */
.sin-herramientas {
  margin-top: 40px;
  text-align: center;
  font-size: 1.2em;
  color: var(--wf-text-muted);
}

/* Grid */
.herramientas-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: 25px;
}

/* Skeleton de carga */
.skeleton-card {
  height: 360px;
  border-radius: 14px;
  background: linear-gradient(90deg, var(--wf-surface) 25%, var(--wf-surface-2) 50%, var(--wf-surface) 75%);
  background-size: 200% 100%;
  animation: shimmer 1.4s infinite;
}
@keyframes shimmer {
  from { background-position: 200% 0; }
  to { background-position: -200% 0; }
}

/* Card */
.herramienta-card {
  background: var(--wf-surface);
  border: 1px solid var(--wf-border);
  border-radius: 14px;
  padding: 18px;
  box-shadow: 0 4px 10px rgba(0,0,0,0.25);
  transition: all 0.3s ease;
  text-align: center;

  display: flex;               /* 👈 convierte la card en contenedor flexible */
  flex-direction: column;
  align-items: center;     /* apila elementos en columna */
  justify-content: flex-start; /* separa imagen/texto arriba y botón abajo */
  height: auto;                /* permite igualar alturas */
  min-height: 360px;
  box-sizing: border-box;
}

.herramienta-card.hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 18px rgba(0, 0, 0, 0.35);
  border-color: var(--wf-accent);
}

.herramienta-link {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-decoration: none;
  color: inherit;
  width: 100%;
}

/* Imagen */
.imagen-wrap {
  position: relative;
  width: 100%;
}

.herramienta-imagen {
  width: 100%;
  height: 200px;
  object-fit: contain;
  border-radius: 12px;
  margin-bottom: 12px;
  background: #fff;
}

.badge-agotado {
  position: absolute;
  top: 8px;
  right: 8px;
  background: var(--wf-accent-2);
  color: #fff;
  font-size: 0.75em;
  font-weight: 700;
  padding: 4px 10px;
  border-radius: 20px;
}

.sin-imagen {
  width: 100%;
  height: 200px;
  background-color: var(--wf-surface-2);
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 12px;
  color: var(--wf-text-muted);
  font-size: 1.1em;
  margin-bottom: 12px;
}

/* Texto */
.herramienta-nombre {
  font-size: 1.1em;
  color: var(--wf-text);
  margin: 6px 0 4px;
  min-height: 2.4em;
}

.herramienta-marca {
  color: var(--wf-text-muted);
  font-size: 0.9em;
  margin-bottom: 5px;
  font-style: italic;
  min-height: 1.8em;
}

.herramienta-precio {
  font-size: 1.2em;
  font-weight: bold;
  color: var(--wf-accent);
  margin: 10px 0;
}

/* Botón */
.btn-agregar {
  margin-top: 1rem;
  width: 100%;
  padding: 12px;
  background: var(--wf-accent);
  color: #161616;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  font-size: 1em;
  font-weight: 700;
  transition: all 0.3s;
}

.btn-agregar.hover {
  background: var(--wf-accent-hover);
  transform: scale(1.03);
}

.btn-agregar:disabled {
  background: var(--wf-border);
  color: var(--wf-text-muted);
  cursor: not-allowed;
  transform: none;
}

/* Cargar más */
.cargar-mas-wrap {
  display: flex;
  justify-content: center;
  margin-top: 36px;
}

.btn-cargar-mas {
  padding: 12px 28px;
  border-radius: 10px;
  border: 1px solid var(--wf-border);
  background: var(--wf-surface);
  color: var(--wf-text);
  font-size: 1em;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-cargar-mas:hover {
  background: var(--wf-accent);
  color: #161616;
  border-color: var(--wf-accent);
}

</style>
