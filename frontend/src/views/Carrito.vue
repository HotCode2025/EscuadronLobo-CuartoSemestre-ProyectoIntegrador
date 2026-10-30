<template>
  <div class="carrito-container">
    <!-- Encabezado -->
    <div class="carrito-header">
      <h1><Icono name="carrito" /> Tu Carrito</h1>
      <button
        v-if="carritoStore.items.length > 0"
        @click="vaciarCarrito"
        class="btn-vaciar"
        @mouseover="hoverBotonVaciar = true"
        @mouseleave="hoverBotonVaciar = false"
        :class="{ hover: hoverBotonVaciar }"
      >
        <Icono name="papelera" /> Vaciar
      </button>
    </div>

    <!-- Carrito vacío -->
    <div v-if="carritoStore.items.length === 0" class="carrito-vacio">
      <p>Parece que tu cesta está vacía...</p>
      <button @click="$router.push('/')" class="btn-ir-catalogo"><Icono name="llave" /> Ir al Catálogo</button>
    </div>

    <!-- Items -->
    <div v-else>
      <div v-for="item in carritoStore.items" :key="item.idHerramienta" class="carrito-item">
        <!-- Imagen -->
        <img
          v-if="item.imagenUrl"
          :src="item.imagenUrl"
          :alt="item.nombre"
          class="item-imagen"
        />
        <div v-else class="sin-imagen"><Icono name="llave" /></div>

        <!-- Info -->
        <div class="item-info">
          <h3>{{ item.nombre }}</h3>
          <p class="marca">{{ item.marca }}</p>
          <p class="precio">${{ formatearPrecio(item.precio) }}</p>
        </div>

        <!-- Acciones: controles, subtotal y eliminar -->
        <div class="item-acciones">
          <div class="item-controles">
            <button @click="decrementar(item)" :disabled="item.cantidad <= 1">−</button>
            <span>{{ item.cantidad }}</span>
            <button
              @click="incrementar(item)"
              :disabled="verificandoStock[item.idHerramienta] || item.cantidad >= (stockDisponible[item.idHerramienta] || 999)"
            >+</button>
          </div>

          <div class="item-subtotal">
            <p>Subtotal:</p>
            <p>${{ formatearPrecio(item.precio * item.cantidad) }}</p>
          </div>

          <button @click="eliminarItem(item.idHerramienta)" class="btn-eliminar"><Icono name="papelera" /></button>
        </div>
      </div>

      <!-- Resumen -->
      <div class="carrito-resumen">
        <div class="resumen-linea">
          <span>Cantidad de items:</span>
          <strong>{{ carritoStore.cantidadTotal }}</strong>
        </div>
        <div class="resumen-linea total">
          <span>Total:</span>
          <strong>${{ formatearPrecio(carritoStore.total) }}</strong>
        </div>
        <button
          @click="crearPedido"
          class="btn-pagar"
          @mouseover="hoverBotonPagar = true"
          @mouseleave="hoverBotonPagar = false"
          :class="{ hover: hoverBotonPagar }"
        >
          <Icono name="tarjeta" /> Proceder al Pago
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.carrito-container {
  max-width: 950px;
  margin: 40px auto;
  padding: 30px;
  background: var(--wf-bg);
  font-family: var(--wf-font-body);
  color: var(--wf-text);
  animation: fadeIn 0.6s ease;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(-10px); }
  to { opacity: 1; transform: translateY(0); }
}

/* Encabezado */
.carrito-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.carrito-header h1 {
  font-size: 2em;
  color: var(--wf-text);
}

/* Carrito vacío */
.carrito-vacio {
  text-align: center;
  padding: 70px 20px;
  background: var(--wf-surface);
  border-radius: 14px;
  border: 2px dashed var(--wf-border);
  color: var(--wf-text-muted);
  font-size: 1.2em;
}

.btn-ir-catalogo {
  margin-top: 20px;
  padding: 12px 24px;
  background: var(--wf-accent);
  color: #161616;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  font-weight: 700;
  transition: 0.3s;
}
.btn-ir-catalogo:hover { background: var(--wf-accent-hover); transform: scale(1.03); }

/* Items */
.carrito-item {
  display: flex;
  align-items: center;
  gap: 16px;
  background: var(--wf-surface);
  border: 1px solid var(--wf-border);
  border-radius: 14px;
  padding: 14px 18px;
  margin-bottom: 16px;
}

.item-imagen {
  width: 80px;
  height: 110px;
  border-radius: 10px;
  object-fit: contain;
  border: 2px solid var(--wf-border);
  background: #fff;
}

.sin-imagen {
  width: 80px;
  height: 110px;
  background: var(--wf-surface-2);
  border-radius: 10px;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 2em;
}

.item-info {
  flex: 1;
}

.item-info h3 {
  margin: 0 0 6px;
  color: var(--wf-text);
  font-size: 1.1em;
}
.item-info .marca {
  margin: 0;
  color: var(--wf-text-muted);
  font-style: italic;
  font-size: 0.9em;
}
.item-info .precio {
  margin-top: 5px;
  color: var(--wf-accent);
  font-weight: bold;
}

.item-acciones {
  display: flex;
  align-items: center;
  gap: 16px;
}

.item-controles {
  display: flex;
  align-items: center;
  gap: 8px;
}

.item-controles button {
  width: 32px;
  height: 32px;
  border-radius: 8px;
  border: none;
  font-size: 18px;
  font-weight: bold;
  cursor: pointer;
  transition: 0.3s;
}

.item-controles button:first-child {
  background: var(--wf-surface-2);
  color: var(--wf-text);
}
.item-controles button:last-child {
  background: var(--wf-accent);
  color: #161616;
}
.item-controles button:hover {
  transform: scale(1.1);
}

.item-subtotal {
  text-align: right;
  min-width: 100px;
}
.item-subtotal p:first-child {
  color: var(--wf-text-muted);
  font-size: 0.9em;
}
.item-subtotal p:last-child {
  font-weight: bold;
  color: var(--wf-accent);
}

/* En mobile, la fila del item se apila para no desbordar el ancho */
@media (max-width: 600px) {
  .carrito-container {
    margin: 20px auto;
    padding: 16px;
  }

  .carrito-header h1 {
    font-size: 1.5em;
  }

  .carrito-item {
    flex-wrap: wrap;
  }

  .item-imagen,
  .sin-imagen {
    width: 64px;
    height: 88px;
  }

  .item-info {
    flex: 1 1 auto;
    min-width: 0;
  }

  .item-info h3 {
    font-size: 1em;
  }

  .item-acciones {
    flex-basis: 100%;
    justify-content: space-between;
    margin-top: 10px;
    padding-top: 10px;
    border-top: 1px dashed var(--wf-border);
  }

  .item-subtotal {
    min-width: auto;
    text-align: left;
  }

  .carrito-resumen {
    padding: 16px;
  }
}

@media (max-width: 380px) {
  .item-imagen,
  .sin-imagen {
    width: 56px;
    height: 76px;
  }

  .item-acciones {
    gap: 10px;
  }
}

/* Botones */
.btn-eliminar {
  padding: 8px 12px;
  background: var(--wf-accent-2);
  color: white;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  font-weight: bold;
  transition: 0.3s;
}
.btn-eliminar:hover {
  background: var(--wf-accent-2-hover);
  transform: scale(1.05);
}

.btn-vaciar {
  background: var(--wf-accent-2);
  color: white;
  border: none;
  border-radius: 10px;
  padding: 10px 20px;
  font-weight: bold;
  cursor: pointer;
  transition: 0.3s;
}
.btn-vaciar.hover {
  transform: scale(1.05);
  background: var(--wf-accent-2-hover);
}

/* Resumen */
.carrito-resumen {
  margin-top: 30px;
  padding: 22px;
  background: var(--wf-surface);
  border-radius: 14px;
  border: 1px solid var(--wf-border);
}

.resumen-linea {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
  font-size: 1.1em;
  color: var(--wf-text);
}
.resumen-linea.total {
  border-top: 2px dashed var(--wf-border);
  padding-top: 10px;
  font-size: 1.3em;
  font-weight: bold;
  color: var(--wf-accent);
}

.btn-pagar {
  width: 100%;
  margin-top: 20px;
  padding: 14px;
  background: var(--wf-accent);
  color: #161616;
  border: none;
  border-radius: 12px;
  font-size: 1.1em;
  font-weight: 700;
  cursor: pointer;
  transition: 0.3s;
}
.btn-pagar.hover {
  transform: scale(1.03);
  background: var(--wf-accent-hover);
}
</style>


<script setup>
import { ref, onMounted } from 'vue'
import { useCarritoStore } from '../stores/carrito'
import { useAuthStore } from '../stores/auth'
import { useToastStore } from '../stores/toast'
import api from '../services/api'
import Icono from '../components/Icono.vue'
import { formatearPrecio } from '../utils/formato'

const carritoStore = useCarritoStore()
const authStore = useAuthStore()
const toastStore = useToastStore()
const stockDisponible = ref({})
const verificandoStock = ref({})
const hoverBotonVaciar = ref(false)
const hoverBotonPagar = ref(false)

onMounted(async () => {
  await verificarStockDeTodos()
})

async function verificarStockDeTodos() {
  for (const item of carritoStore.items) {
    await verificarStock(item.idHerramienta)
  }
}

async function verificarStock(idHerramienta) {
  try {
    verificandoStock.value[idHerramienta] = true
    const { data: herramienta } = await api.get(`/herramientas/obtener/${idHerramienta}`)
    stockDisponible.value[idHerramienta] = herramienta.stock ?? 999
  } catch (err) {
    console.error('Error al verificar stock:', err)
    stockDisponible.value[idHerramienta] = 999
  } finally {
    verificandoStock.value[idHerramienta] = false
  }
}

async function incrementar(item) {
  await verificarStock(item.idHerramienta)
  const stockActual = stockDisponible.value[item.idHerramienta] ?? 999
  if (item.cantidad >= stockActual) {
    toastStore.error(`No hay más stock disponible. Stock actual: ${stockActual}`)
    return
  }
  carritoStore.incrementar(item.idHerramienta)
}

function decrementar(item) {
  if (item.cantidad > 1) carritoStore.decrementar(item.idHerramienta)
}

function eliminarItem(idHerramienta) {
  if (confirm('¿Eliminar esta herramienta del carrito?')) carritoStore.eliminar(idHerramienta)
}

function vaciarCarrito() {
  if (confirm('¿Vaciar todo el carrito?')) carritoStore.limpiar()
}

async function crearPedido() {
  try {
    if (!authStore.user || !authStore.user.idUsuario) {
      toastStore.error('Debés iniciar sesión para continuar')
      return
    }

    for (const item of carritoStore.items) {
      await verificarStock(item.idHerramienta)
      const stockActual = stockDisponible.value[item.idHerramienta] ?? 999
      if (item.cantidad > stockActual) {
        toastStore.error(`Stock insuficiente para "${item.nombre}". Disponible: ${stockActual}`)
        return
      }
    }

    const herramientas = carritoStore.items.map(i => ({ idHerramienta: i.idHerramienta, cantidad: i.cantidad }))
    const { data: pedido } = await api.post('/pedidos/crear', {
      idUsuario: authStore.user.idUsuario,
      herramientas
    })

    const { data: pago } = await api.post(`/pagos/crear-preferencia/${pedido.idPedido}`)
    carritoStore.limpiar()
    window.location.href = pago.initPoint
  } catch (err) {
    toastStore.error('Error: ' + (err.response?.data?.message || err.message))
  }
}
</script>
