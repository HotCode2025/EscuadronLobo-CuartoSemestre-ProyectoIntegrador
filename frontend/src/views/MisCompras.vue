<template>
  <div class="compras-container">
    <h1><Icono name="caja" /> Mis Compras</h1>

    <div v-if="pedidos.length === 0" class="sin-pedidos">
      <p>No tenés compras todavía</p>
    </div>

    <div v-for="pedido in pedidos" :key="pedido.idPedido" class="pedido-card">
      <h3>Pedido #{{ pedido.idPedido }}</h3>
      <p><strong>Fecha:</strong> {{ formatearFecha(pedido.creadoEn) }}</p>
      <p><strong>Total:</strong> ${{ formatearPrecio(pedido.precioTotal) }}</p>

      <h4 class="subtitulo"><Icono name="llave" /> Detalle de herramientas:</h4>

      <div v-for="item in pedido.herramientas" :key="item.idHerramientaPedido" class="item-linea">
        <p>
          <span class="item-nombre">{{ item.nombreHerramienta }}</span><br>
          Cantidad: {{ item.cantidad }} |
          Subtotal: ${{ formatearPrecio(item.precioTotal) }} |
          Estado:
          <span class="estado" :style="{ color: getEstadoColor(item.estadoPedido) }">
            {{ item.estadoPedido }}
          </span>
        </p>
        <button
          v-if="item.estadoPedido === 'ENVIANDO'"
          class="btn-confirmar"
          :disabled="confirmando[item.idHerramientaPedido]"
          @click="confirmarRecepcion(pedido, item)"
        >
          <Icono name="check-circulo" /> Confirmar que lo recibí
        </button>

        <button
          v-if="item.estadoPedido === 'ENTREGADO' && !item.solicitudDevolucion"
          class="btn-devolucion"
          :disabled="solicitando[item.idHerramientaPedido]"
          @click="solicitarDevolucion(pedido, item)"
        >
          Solicitar devolución
        </button>
        <p v-else-if="item.solicitudDevolucion" class="aviso-devolucion">
          Devolución solicitada, en revisión.
        </p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '../services/api'
import { useAuthStore } from '../stores/auth'
import { useToastStore } from '../stores/toast'
import Icono from '../components/Icono.vue'
import { formatearPrecio } from '../utils/formato'

const authStore = useAuthStore()
const toastStore = useToastStore()
const pedidos = ref([])
const confirmando = ref({})
const solicitando = ref({})

onMounted(async () => {
  try {
    const { data } = await api.get('/pedidos/mis-pedidos')
    pedidos.value = data.sort((a, b) => new Date(b.creadoEn) - new Date(a.creadoEn))
  } catch (err) {
    console.error('Error al cargar pedidos:', err)
  }
})

function formatearFecha(fechaISO) {
  if (!fechaISO) return 'N/A'
  const fecha = new Date(fechaISO)
  return fecha.toLocaleDateString('es-AR', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  })
}

function getEstadoColor(estado) {
  const colores = {
    'PENDIENTE': '#F98103',
    'CONFIRMADO': '#4DA3FF',
    'ENVIANDO': '#4DA3FF',
    'ENTREGADO': '#4CAF6D',
    'CANCELADO': '#D63831',
    'DEVUELTO': '#D63831'
  }
  return colores[estado] || '#9D9D9D'
}

async function confirmarRecepcion(pedido, item) {
  confirmando.value[item.idHerramientaPedido] = true
  try {
    await api.put(`/pedidos/${pedido.idPedido}/herramienta-pedido/${item.idHerramientaPedido}/confirmar-recepcion`)
    item.estadoPedido = 'ENTREGADO'
    toastStore.exito('¡Gracias por confirmar! Pedido marcado como entregado.')
  } catch (err) {
    const errorMsg = err.response?.data?.message || err.message
    toastStore.error('No se pudo confirmar: ' + errorMsg)
  } finally {
    confirmando.value[item.idHerramientaPedido] = false
  }
}

async function solicitarDevolucion(pedido, item) {
  solicitando.value[item.idHerramientaPedido] = true
  try {
    await api.put(`/pedidos/${pedido.idPedido}/herramienta-pedido/${item.idHerramientaPedido}/solicitar-devolucion`)
    item.solicitudDevolucion = true
    toastStore.exito('Solicitud enviada. El admin la va a revisar.')
  } catch (err) {
    const errorMsg = err.response?.data?.message || err.message
    toastStore.error('No se pudo solicitar la devolución: ' + errorMsg)
  } finally {
    solicitando.value[item.idHerramientaPedido] = false
  }
}
</script>

<style scoped>
.compras-container {
  max-width: 800px;
  margin: 40px auto;
  background: var(--wf-bg);
  padding: 30px;
  font-family: var(--wf-font-body);
  color: var(--wf-text);
}

.compras-container h1 {
  text-align: center;
  color: var(--wf-text);
  font-size: 2em;
  margin-bottom: 20px;
}

.sin-pedidos {
  text-align: center;
  padding: 20px;
  color: var(--wf-text-muted);
}

.pedido-card {
  background: var(--wf-surface);
  border: 1px solid var(--wf-border);
  padding: 20px;
  margin: 20px 0;
  border-radius: 12px;
  transition: transform 0.2s;
}
.pedido-card:hover {
  transform: scale(1.01);
}

.pedido-card h3 {
  margin-bottom: 8px;
  color: var(--wf-text);
}

.subtitulo {
  margin-top: 15px;
  color: var(--wf-text-muted);
}

.item-linea {
  margin-left: 20px;
  padding: 10px 0;
  border-bottom: 1px dashed var(--wf-border);
}

.item-linea p {
  margin: 0;
}

.item-nombre {
  font-weight: 600;
  color: var(--wf-text);
}

.estado {
  font-weight: bold;
}

.btn-confirmar {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  margin-top: 8px;
  padding: 8px 14px;
  background: var(--wf-success, #4CAF6D);
  color: #0E1B14;
  border: none;
  border-radius: 8px;
  font-weight: 700;
  font-size: 0.85em;
  cursor: pointer;
  transition: filter 0.2s;
}
.btn-confirmar:hover:not(:disabled) {
  filter: brightness(1.1);
}
.btn-confirmar:disabled {
  opacity: 0.6;
  cursor: wait;
}

.btn-devolucion {
  display: inline-block;
  margin-top: 8px;
  padding: 8px 14px;
  background: transparent;
  color: var(--wf-accent-2);
  border: 1px solid var(--wf-accent-2);
  border-radius: 8px;
  font-weight: 700;
  font-size: 0.85em;
  cursor: pointer;
  transition: all 0.2s;
}
.btn-devolucion:hover:not(:disabled) {
  background: var(--wf-accent-2);
  color: #fff;
}
.btn-devolucion:disabled {
  opacity: 0.6;
  cursor: wait;
}

.aviso-devolucion {
  margin: 8px 0 0;
  color: var(--wf-text-muted);
  font-size: 0.85em;
  font-style: italic;
}

@media (max-width: 600px) {
  .compras-container {
    margin: 20px auto;
    padding: 16px;
  }

  .compras-container h1 {
    font-size: 1.5em;
  }

  .pedido-card {
    padding: 14px;
  }

  .item-linea {
    margin-left: 8px;
  }

  .btn-confirmar,
  .btn-devolucion {
    width: 100%;
    justify-content: center;
    padding: 10px 14px;
  }
}
</style>
