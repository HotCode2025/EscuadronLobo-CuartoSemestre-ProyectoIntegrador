<template>
  <div class="admin-page">
    <h1 class="titulo"><Icono name="llave" /> Gestión de Herramientas</h1>

    <AdminNav />

    <div class="acciones">
      <button class="btn-crear" @click="abrirCrear">
        + Crear Herramienta
      </button>
    </div>

    <!-- MODAL: formulario de crear/editar -->
    <div v-if="mostrarForm" class="modal-overlay" @click.self="cancelar">
      <form @submit.prevent="guardar" class="form-card">
        <h3>{{ editandoId ? 'Editar Herramienta' : 'Nueva Herramienta' }}</h3>

        <input v-model="form.nombre" placeholder="Nombre" required />
        <input v-model="form.marca" placeholder="Marca" required />
        <textarea v-model="form.descripcion" placeholder="Descripción"></textarea>

        <label class="campo-label">Precio ($)</label>
        <input v-model="form.precio" type="number" step="0.01" min="0.01" required />

        <label class="campo-label">Stock (unidades)</label>
        <input v-model.number="form.stock" type="number" min="0" required />

        <label class="campo-label">Descuento % (0 = sin oferta)</label>
        <input v-model.number="form.descuento" type="number" min="0" max="90" />

        <select v-model.number="form.idCategoria" required>
          <option disabled value="">Seleccionar categoría</option>
          <option v-for="cat in categorias" :key="cat.idCategoria" :value="cat.idCategoria">
            {{ cat.nombre }}
          </option>
        </select>

        <input v-model="form.imagenUrl" placeholder="URL Imagen" />

        <label class="campo-label">Características técnicas (una por línea)</label>
        <textarea v-model="form.caracteristicasTexto" class="textarea-caracteristicas" placeholder="Potencia: 2100W&#10;Alimentación: 220V ~ 50Hz"></textarea>

        <div class="form-botones">
          <button type="submit" class="btn-guardar">{{ editandoId ? 'Actualizar' : 'Guardar' }}</button>
          <button type="button" class="btn-cancelar" @click="cancelar">Cancelar</button>
        </div>
      </form>
    </div>

    <!-- BUSCADOR -->
    <div class="buscador">
      <input v-model="busqueda" type="text" placeholder="Buscar por nombre o marca..." class="input-buscar" />
      <span class="contador">{{ herramientasFiltradas.length }} de {{ herramientas.length }}</span>
    </div>

    <!-- TABLA DE HERRAMIENTAS -->
    <div class="tabla-wrap">
      <table class="tabla">
        <thead>
          <tr>
            <th>Herramienta</th>
            <th>Categoría</th>
            <th>Precio</th>
            <th>Stock</th>
            <th>Oferta</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="herramienta in herramientasFiltradas" :key="herramienta.idHerramienta">
            <td data-label="Herramienta">
              <strong>{{ herramienta.nombre }}</strong>
              <span class="marca-chica">{{ herramienta.marca }}</span>
            </td>
            <td data-label="Categoría">{{ herramienta.categoria?.nombre || '—' }}</td>
            <td data-label="Precio">${{ formatearPrecio(herramienta.precio) }}</td>
            <td data-label="Stock">
              <span :class="{ 'stock-agotado': herramienta.stock === 0 }">{{ herramienta.stock ?? 0 }}</span>
            </td>
            <td data-label="Oferta">
              <span v-if="herramienta.descuento > 0" class="badge-oferta">{{ herramienta.descuento }}% OFF</span>
              <span v-else class="sin-oferta">—</span>
            </td>
            <td data-label="Acciones" class="col-acciones">
              <button @click="abrirEditar(herramienta)" class="btn-editar"><Icono name="lapiz" /> Editar</button>
              <button @click="eliminar(herramienta.idHerramienta)" class="btn-eliminar"><Icono name="papelera" /></button>
            </td>
          </tr>
          <tr v-if="herramientasFiltradas.length === 0">
            <td colspan="6" class="sin-resultados">No se encontraron herramientas.</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import api from '../services/api'
import { useToastStore } from '../stores/toast'
import AdminNav from '../components/AdminNav.vue'
import Icono from '../components/Icono.vue'
import { formatearPrecio } from '../utils/formato'

const toastStore = useToastStore()
const herramientas = ref([])
const categorias = ref([])
const mostrarForm = ref(false)
const editandoId = ref(null)
const busqueda = ref('')

const herramientasFiltradas = computed(() => {
  const texto = busqueda.value.trim().toLowerCase()
  if (!texto) return herramientas.value
  return herramientas.value.filter(h =>
    h.nombre?.toLowerCase().includes(texto) || h.marca?.toLowerCase().includes(texto)
  )
})

const formInicial = () => ({
  nombre: '',
  marca: '',
  descripcion: '',
  precio: 0,
  stock: 0,
  descuento: 0,
  idCategoria: '',
  imagenUrl: '',
  caracteristicasTexto: ''
})

const form = ref(formInicial())

onMounted(async () => {
  await Promise.all([cargarHerramientas(), cargarCategorias()])
})

async function cargarHerramientas() {
  try {
    const { data } = await api.get('/herramientas/listar')
    herramientas.value = data
  } catch (err) {
    console.error('Error al cargar herramientas:', err)
    toastStore.error('No se pudieron cargar las herramientas')
  }
}

async function cargarCategorias() {
  try {
    const { data } = await api.get('/categorias/listar')
    categorias.value = data
  } catch (err) {
    console.error('Error al cargar categorías:', err)
  }
}

function abrirCrear() {
  editandoId.value = null
  form.value = formInicial()
  mostrarForm.value = true
}

function abrirEditar(herramienta) {
  editandoId.value = herramienta.idHerramienta
  form.value = {
    nombre: herramienta.nombre,
    marca: herramienta.marca,
    descripcion: herramienta.descripcion || '',
    precio: herramienta.precio,
    stock: herramienta.stock ?? 0,
    descuento: herramienta.descuento ?? 0,
    idCategoria: herramienta.categoria?.idCategoria ?? '',
    imagenUrl: herramienta.imagenUrl || '',
    caracteristicasTexto: (herramienta.caracteristicas || []).join('\n')
  }
  mostrarForm.value = true
}

function cancelar() {
  mostrarForm.value = false
  editandoId.value = null
  form.value = formInicial()
}

async function guardar() {
  try {
    const { caracteristicasTexto, ...resto } = form.value
    const payload = {
      ...resto,
      caracteristicas: caracteristicasTexto
        .split('\n')
        .map(linea => linea.trim())
        .filter(linea => linea.length > 0)
    }

    if (editandoId.value) {
      await api.put(`/herramientas/actualizar/${editandoId.value}`, payload)
      toastStore.exito('Herramienta actualizada correctamente')
    } else {
      await api.post('/herramientas/crear', payload)
      toastStore.exito('Herramienta creada correctamente')
    }
    cancelar()
    await cargarHerramientas()
  } catch (err) {
    const errorMsg = err.response?.data?.message || err.message
    toastStore.error('Error al guardar: ' + errorMsg)
  }
}

async function eliminar(id) {
  if (!confirm('¿Eliminar esta herramienta? Esta acción no se puede deshacer.')) return
  try {
    await api.delete(`/herramientas/eliminar/${id}`)
    herramientas.value = herramientas.value.filter(l => l.idHerramienta !== id)
    toastStore.exito('Herramienta eliminada')
  } catch (err) {
    const errorMsg = err.response?.data?.message || err.message
    toastStore.error('Error al eliminar herramienta: ' + errorMsg)
  }
}
</script>

<style scoped>
.admin-page {
  background: var(--wf-bg);
  min-height: 100vh;
  padding: 40px;
  font-family: var(--wf-font-body);
  color: var(--wf-text);
}

.titulo {
  text-align: center;
  background: var(--wf-surface);
  border: 1px solid var(--wf-border);
  border-radius: 12px;
  padding: 14px;
  margin-bottom: 30px;
  color: var(--wf-text);
}

.acciones {
  text-align: center;
  margin-bottom: 20px;
}

.btn-crear {
  background: var(--wf-accent);
  color: #161616;
  border: none;
  font-weight: 700;
  padding: 10px 20px;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.2s ease-in-out;
}
.btn-crear:hover {
  background: var(--wf-accent-hover);
}

.modal-overlay {
  position: fixed;
  inset: 0;
  background: rgba(10, 15, 20, 0.7);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  z-index: 100;
}

.form-card {
  background: var(--wf-surface);
  border: 1px solid var(--wf-border);
  padding: 24px;
  border-radius: 12px;
  width: 100%;
  max-width: 460px;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.4);
}

.form-card h3 {
  text-align: center;
  margin-bottom: 15px;
  color: var(--wf-text);
}

.form-card input,
.form-card textarea,
.form-card select {
  display: block;
  width: 100%;
  margin: 8px 0;
  padding: 8px;
  border-radius: 6px;
  border: 1px solid var(--wf-border);
  background: var(--wf-bg-deep);
  color: var(--wf-text);
  box-sizing: border-box;
  font-family: inherit;
  font-size: 1em;
}

.textarea-caracteristicas {
  min-height: 90px;
  resize: vertical;
}

.campo-label {
  display: block;
  margin: 10px 0 2px;
  font-size: 0.82em;
  font-weight: 600;
  color: var(--wf-text-muted);
}

.form-botones {
  display: flex;
  gap: 10px;
  margin-top: 10px;
}

.btn-guardar {
  flex: 1;
  background: var(--wf-accent);
  color: #161616;
  border: none;
  padding: 10px 20px;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 700;
}
.btn-guardar:hover {
  background: var(--wf-accent-hover);
}

.btn-cancelar {
  background: transparent;
  color: var(--wf-text-muted);
  border: 1px solid var(--wf-border);
  padding: 10px 20px;
  border-radius: 8px;
  cursor: pointer;
}
.btn-cancelar:hover {
  background: var(--wf-bg-deep);
}

.buscador {
  max-width: 900px;
  margin: 0 auto 16px;
  display: flex;
  align-items: center;
  gap: 14px;
}

.input-buscar {
  flex: 1;
  padding: 10px 14px;
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

.contador {
  color: var(--wf-text-muted);
  font-size: 0.9em;
  white-space: nowrap;
}

.tabla-wrap {
  max-width: 900px;
  margin: 0 auto;
  overflow-x: auto;
  border: 1px solid var(--wf-border);
  border-radius: 10px;
}

.tabla {
  width: 100%;
  border-collapse: collapse;
  background: var(--wf-surface);
}

.tabla th {
  text-align: left;
  padding: 12px 16px;
  background: var(--wf-surface-2);
  color: var(--wf-text-muted);
  font-size: 0.8em;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  white-space: nowrap;
}

.tabla td {
  padding: 12px 16px;
  border-top: 1px solid var(--wf-border);
  color: var(--wf-text);
  vertical-align: middle;
}

.marca-chica {
  display: block;
  color: var(--wf-text-muted);
  font-size: 0.85em;
  font-style: italic;
}

.stock-agotado {
  color: var(--wf-accent-2);
  font-weight: 700;
}

.badge-oferta {
  background: var(--wf-accent-2);
  color: #fff;
  font-size: 0.78em;
  font-weight: 700;
  padding: 3px 8px;
  border-radius: 20px;
  white-space: nowrap;
}

.sin-oferta {
  color: var(--wf-text-muted);
}

.col-acciones {
  display: flex;
  gap: 8px;
  white-space: nowrap;
}

.btn-editar {
  background: var(--wf-surface-2);
  color: var(--wf-text);
  border: 1px solid var(--wf-border);
  border-radius: 8px;
  padding: 6px 12px;
  cursor: pointer;
}
.btn-editar:hover {
  background: var(--wf-border);
}

.btn-eliminar {
  background: var(--wf-accent-2);
  color: white;
  border: none;
  border-radius: 8px;
  padding: 6px 10px;
  cursor: pointer;
}
.btn-eliminar:hover {
  background: var(--wf-accent-2-hover);
}

.sin-resultados {
  text-align: center;
  color: var(--wf-text-muted);
  padding: 24px;
}

@media (max-width: 700px) {
  .admin-page {
    padding: 16px;
  }

  .titulo {
    font-size: 1.3em;
    padding: 12px 10px;
  }

  .buscador {
    flex-direction: column;
    align-items: stretch;
    gap: 8px;
  }

  .form-card {
    padding: 18px;
  }

  /* La tabla pasa a ser una lista de tarjetas: cada <tr> es una tarjeta
     y cada <td> muestra su propia etiqueta (data-label) arriba del valor. */
  .tabla-wrap {
    overflow-x: visible;
    border: none;
  }

  .tabla,
  .tabla thead,
  .tabla tbody,
  .tabla th,
  .tabla td,
  .tabla tr {
    display: block;
    width: 100%;
  }

  .tabla thead {
    position: absolute;
    width: 1px;
    height: 1px;
    overflow: hidden;
    clip: rect(0 0 0 0);
    white-space: nowrap;
  }

  .tabla tr {
    background: var(--wf-surface);
    border: 1px solid var(--wf-border);
    border-radius: 10px;
    margin-bottom: 12px;
    padding: 6px 14px;
  }

  .tabla tr:has(.sin-resultados) {
    padding: 0;
    border: none;
    background: none;
  }

  .tabla td {
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 10px;
    padding: 10px 0;
    border-top: 1px dashed var(--wf-border);
    text-align: right;
  }

  .tabla td:first-child {
    border-top: none;
  }

  .tabla td::before {
    content: attr(data-label);
    color: var(--wf-text-muted);
    font-size: 0.78em;
    font-weight: 700;
    text-transform: uppercase;
    letter-spacing: 0.04em;
    text-align: left;
    flex: none;
  }

  .tabla td[data-label='Herramienta'] {
    flex-direction: column;
    align-items: flex-start;
    text-align: left;
  }

  .tabla td[data-label='Herramienta']::before {
    margin-bottom: 4px;
  }

  .col-acciones {
    justify-content: flex-end;
  }

  .sin-resultados {
    padding: 24px 0;
  }
}
</style>
