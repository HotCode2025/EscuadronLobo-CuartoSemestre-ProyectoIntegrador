<template>
  <div class="admin-page">
    <h1 class="titulo"><Icono name="engranaje" /> Panel de Administrador</h1>

    <AdminNav />

    <!-- Resumen -->
    <div class="resumen-grid">
      <div class="resumen-card">
        <p class="resumen-numero">{{ pedidos.length }}</p>
        <p class="resumen-label">Pedidos totales</p>
      </div>
      <div class="resumen-card">
        <p class="resumen-numero">${{ formatearPrecio(totalFacturado) }}</p>
        <p class="resumen-label">Total facturado</p>
      </div>
      <div class="resumen-card">
        <p class="resumen-numero">{{ cantidadPorEstado.PENDIENTE || 0 }}</p>
        <p class="resumen-label">Con items pendientes</p>
      </div>
    </div>

    <h2 class="subtitulo">Pedidos</h2>

    <!-- Filtro por estado -->
    <div class="filtros">
      <button
        class="filtro-btn"
        :class="{ activo: filtroEstado === null }"
        @click="filtroEstado = null"
      >
        Todos ({{ pedidos.length }})
      </button>
      <button
        v-for="estado in estadosDisponibles"
        :key="estado"
        class="filtro-btn"
        :class="{ activo: filtroEstado === estado }"
        @click="filtroEstado = estado"
      >
        {{ estado }} ({{ cantidadPorEstado[estado] || 0 }})
      </button>
    </div>

    <div v-if="pedidosFiltrados.length === 0" class="sin-pedidos">
      No hay pedidos{{ filtroEstado ? ` con estado ${filtroEstado}` : '' }}.
    </div>

    <div v-for="pedido in pedidosFiltrados" :key="pedido.idPedido" class="pedido-card">
      <div class="pedido-header">
        <h3>Pedido #{{ pedido.idPedido }}</h3>
        <button
          v-if="puedeCancelarse(pedido)"
          class="btn-cancelar-pedido"
          :disabled="cancelando[pedido.idPedido]"
          @click="cancelarPedido(pedido)"
        >
          Cancelar pedido
        </button>
      </div>
      <p><strong>Usuario:</strong> {{ pedido.usuario?.nombre }} ({{ pedido.usuario?.email }})</p>
      <p><strong>Fecha:</strong> {{ new Date(pedido.creadoEn).toLocaleDateString() }}</p>
      <p><strong>Total:</strong> ${{ formatearPrecio(pedido.precioTotal) }}</p>

      <div v-for="item in pedido.herramientas" :key="item.idHerramientaPedido" class="item-linea">
        <span>
          <Icono name="llave" /> <strong>{{ item.nombreHerramienta }}</strong> ×{{ item.cantidad }}
          <span v-if="item.solicitudDevolucion" class="badge-solicitud">⚠ Devolución solicitada</span>
        </span>
        <div class="item-acciones">
          <span
            v-if="item.estadoPedido === 'CANCELADO' || item.estadoPedido === 'DEVUELTO'"
            class="estado-final"
            :style="{ color: colorEstado(item.estadoPedido) }"
          >
            {{ item.estadoPedido }}
          </span>
          <select
            v-else
            class="select-estado"
            :style="{ color: colorEstado(item.estadoPedido) }"
            :value="item.estadoPedido"
            :disabled="cambiando[item.idHerramientaPedido]"
            @change="cambiarEstado(pedido, item, $event.target.value)"
          >
            <option v-for="estado in estadosDropdown" :key="estado" :value="estado">{{ estado }}</option>
          </select>
          <button
            v-if="item.estadoPedido === 'ENTREGADO'"
            class="btn-devuelto"
            :disabled="cambiando[item.idHerramientaPedido]"
            @click="marcarDevuelto(pedido, item)"
          >
            Marcar devuelto
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import api from '../services/api'
import AdminNav from '../components/AdminNav.vue'
import Icono from '../components/Icono.vue'
import { formatearPrecio } from '../utils/formato'
import { useToastStore } from '../stores/toast'

const toastStore = useToastStore()
const pedidos = ref([])
const filtroEstado = ref(null)
const cambiando = ref({})
const cancelando = ref({})

// Todos los estados posibles (para el filtro de arriba)
const estadosDisponibles = ['PENDIENTE', 'CONFIRMADO', 'ENVIANDO', 'ENTREGADO', 'CANCELADO', 'DEVUELTO']
// Solo los de flujo normal (para el <select> de cambio manual).
// CANCELADO y DEVUELTO tienen sus propios botones con reglas y reposición de stock.
const estadosDropdown = ['PENDIENTE', 'CONFIRMADO', 'ENVIANDO', 'ENTREGADO']

onMounted(async () => {
  try {
    const { data } = await api.get('/pedidos/listar')
    pedidos.value = data
  } catch (err) {
    console.error('Error al cargar pedidos:', err)
  }
})

// Suma solo los items con pago confirmado (excluye cancelados, devueltos y pendientes de pago)
const totalFacturado = computed(() => {
  let total = 0
  for (const pedido of pedidos.value) {
    for (const item of pedido.herramientas || []) {
      if (!['CANCELADO', 'DEVUELTO', 'PENDIENTE'].includes(item.estadoPedido)) {
        total += item.precioTotal || 0
      }
    }
  }
  return total
})

// Cuenta pedidos que tienen al menos un item en cada estado
const cantidadPorEstado = computed(() => {
  const conteo = {}
  for (const pedido of pedidos.value) {
    const estadosDelPedido = new Set((pedido.herramientas || []).map(i => i.estadoPedido))
    for (const estado of estadosDelPedido) {
      conteo[estado] = (conteo[estado] || 0) + 1
    }
  }
  return conteo
})

const pedidosFiltrados = computed(() => {
  if (!filtroEstado.value) return pedidos.value
  return pedidos.value.filter(p =>
    (p.herramientas || []).some(i => i.estadoPedido === filtroEstado.value)
  )
})

async function cambiarEstado(pedido, item, nuevoEstado) {
  const estadoAnterior = item.estadoPedido
  cambiando.value[item.idHerramientaPedido] = true
  try {
    await api.put(
      `/pedidos/actualizar-estado/${pedido.idPedido}/herramienta-pedido/${item.idHerramientaPedido}`,
      null,
      { params: { nuevoEstado } }
    )
    item.estadoPedido = nuevoEstado
    toastStore.exito(`Estado actualizado a ${nuevoEstado}`)
  } catch (err) {
    item.estadoPedido = estadoAnterior
    const errorMsg = err.response?.data?.message || err.message
    toastStore.error('No se pudo actualizar el estado: ' + errorMsg)
  } finally {
    cambiando.value[item.idHerramientaPedido] = false
  }
}

function puedeCancelarse(pedido) {
  return (pedido.herramientas || []).every(
    i => i.estadoPedido === 'PENDIENTE' || i.estadoPedido === 'CONFIRMADO'
  )
}

async function cancelarPedido(pedido) {
  if (!confirm(`¿Cancelar el pedido #${pedido.idPedido}? Esta acción no se puede deshacer.`)) return
  cancelando.value[pedido.idPedido] = true
  try {
    const { data } = await api.put(`/pedidos/cancelar/${pedido.idPedido}`)
    pedido.herramientas = data.herramientas
    toastStore.exito('Pedido cancelado correctamente')
  } catch (err) {
    const errorMsg = err.response?.data?.message || err.message
    toastStore.error('No se pudo cancelar: ' + errorMsg)
  } finally {
    cancelando.value[pedido.idPedido] = false
  }
}

async function marcarDevuelto(pedido, item) {
  if (!confirm(`¿Marcar "${item.nombreHerramienta}" como devuelto? Esto repone el stock y no se puede deshacer.`)) return
  cambiando.value[item.idHerramientaPedido] = true
  try {
    await api.put(`/pedidos/${pedido.idPedido}/herramienta-pedido/${item.idHerramientaPedido}/marcar-devuelto`)
    item.estadoPedido = 'DEVUELTO'
    item.solicitudDevolucion = false
    toastStore.exito('Producto marcado como devuelto, stock repuesto')
  } catch (err) {
    const errorMsg = err.response?.data?.message || err.message
    toastStore.error('No se pudo marcar como devuelto: ' + errorMsg)
  } finally {
    cambiando.value[item.idHerramientaPedido] = false
  }
}

function colorEstado(estado) {
  const colores = {
    PENDIENTE: '#F98103',
    CONFIRMADO: '#4DA3FF',
    ENVIANDO: '#4DA3FF',
    ENTREGADO: '#4CAF6D',
    CANCELADO: '#D63831',
    DEVUELTO: '#D63831'
  }
  return colores[estado] || '#9D9D9D'
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
  color: var(--wf-text);
  background: var(--wf-surface);
  padding: 14px 0;
  border-radius: 12px;
  border: 1px solid var(--wf-border);
  margin-bottom: 20px;
}

.resumen-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(160px, 1fr));
  gap: 14px;
  max-width: 700px;
  margin: 0 auto 30px;
}

.resumen-card {
  background: var(--wf-surface);
  border: 1px solid var(--wf-border);
  border-radius: 10px;
  padding: 16px;
  text-align: center;
}

.resumen-numero {
  font-size: 1.8em;
  font-weight: 700;
  color: var(--wf-accent);
  margin: 0;
}

.resumen-label {
  color: var(--wf-text-muted);
  font-size: 0.85em;
  margin: 4px 0 0;
}

.subtitulo {
  color: var(--wf-text);
  display: block;
  text-align: center;
  margin-bottom: 15px;
}

.filtros {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  justify-content: center;
  margin-bottom: 20px;
}

.filtro-btn {
  padding: 6px 14px;
  border-radius: 20px;
  border: 1px solid var(--wf-border);
  background: var(--wf-surface);
  color: var(--wf-text-muted);
  cursor: pointer;
  font-size: 0.85em;
  transition: all 0.2s ease-in-out;
}

.filtro-btn:hover {
  background: var(--wf-surface-2);
}

.filtro-btn.activo {
  background: var(--wf-accent);
  color: #161616;
  border-color: var(--wf-accent);
  font-weight: 700;
}

.sin-pedidos {
  text-align: center;
  color: var(--wf-text-muted);
  font-style: italic;
  margin-top: 20px;
}

.pedido-card {
  background: var(--wf-surface);
  border: 1px solid var(--wf-border);
  padding: 15px 20px;
  margin: 15px 0;
  border-radius: 10px;
  transition: transform 0.2s ease-in-out;
}
.pedido-card:hover {
  transform: scale(1.01);
}

.pedido-card h3 {
  margin-bottom: 8px;
  color: var(--wf-text);
}

.pedido-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 10px;
  flex-wrap: wrap;
}

.btn-cancelar-pedido {
  background: var(--wf-accent-2);
  color: #fff;
  border: none;
  border-radius: 8px;
  padding: 6px 12px;
  font-size: 0.8em;
  font-weight: 700;
  cursor: pointer;
}
.btn-cancelar-pedido:hover:not(:disabled) {
  background: var(--wf-accent-2-hover);
}
.btn-cancelar-pedido:disabled {
  opacity: 0.6;
  cursor: wait;
}

.item-linea {
  background: var(--wf-surface-2);
  padding: 8px 10px;
  border-radius: 6px;
  margin: 5px 0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 10px;
  flex-wrap: wrap;
}

.item-acciones {
  display: flex;
  align-items: center;
  gap: 8px;
}

.estado-final {
  font-weight: 700;
  font-size: 0.85em;
  padding: 4px 10px;
  border: 1px solid currentColor;
  border-radius: 6px;
}

.badge-solicitud {
  display: inline-block;
  margin-left: 8px;
  background: var(--wf-accent-2);
  color: #fff;
  font-size: 0.72em;
  font-weight: 700;
  padding: 2px 8px;
  border-radius: 20px;
  vertical-align: middle;
}

.btn-devuelto {
  background: var(--wf-surface);
  border: 1px solid var(--wf-accent-2);
  color: var(--wf-accent-2);
  border-radius: 6px;
  padding: 4px 10px;
  font-size: 0.8em;
  font-weight: 700;
  cursor: pointer;
}
.btn-devuelto:hover:not(:disabled) {
  background: var(--wf-accent-2);
  color: #fff;
}
.btn-devuelto:disabled {
  opacity: 0.6;
  cursor: wait;
}

.select-estado {
  background: var(--wf-bg-deep);
  border: 1px solid var(--wf-border);
  border-radius: 6px;
  padding: 4px 8px;
  font-weight: 700;
  font-size: 0.85em;
  cursor: pointer;
}
.select-estado:disabled {
  opacity: 0.6;
  cursor: wait;
}

@media (max-width: 600px) {
  .admin-page {
    padding: 16px;
  }

  .titulo {
    font-size: 1.3em;
    padding: 12px 10px;
  }

  .filtros {
    flex-direction: column;
    align-items: stretch;
  }

  .filtro-btn {
    padding: 10px 14px;
    text-align: center;
  }

  .resumen-grid {
    grid-template-columns: repeat(2, 1fr);
    max-width: 100%;
  }

  .pedido-card {
    padding: 12px 14px;
  }

  .item-linea {
    flex-direction: column;
    align-items: stretch;
  }

  .item-acciones {
    justify-content: space-between;
  }

  .select-estado,
  .btn-devuelto,
  .btn-cancelar-pedido {
    padding: 8px 12px;
  }
}
</style>
