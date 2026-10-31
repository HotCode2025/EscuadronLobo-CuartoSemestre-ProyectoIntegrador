<template>
  <div class="pago-page success">
    <div class="pago-card">
      <div class="icono-wrap">
        <svg viewBox="0 0 24 24" class="icono">
          <path
            fill="currentColor"
            d="M12 2C6.5 2 2 6.5 2 12S6.5 22 12 22 22 17.5 22 12 17.5 2 12 2M10 17L5 12L6.41 10.59L10 14.17L17.59 6.58L19 8L10 17Z"
          />
        </svg>
        <div class="halo"></div>
      </div>

      <h1><Icono name="check-circulo" /> Pago Exitoso</h1>
      <p class="subtexto">Tu pago fue procesado correctamente.</p>

      <div v-if="paymentInfo" class="info-box">
        <p v-if="paymentInfo.payment_id"><strong>ID de Pago:</strong> {{ paymentInfo.payment_id }}</p>
        <p><strong>Estado:</strong> {{ paymentInfo.status || 'approved' }}</p>
        <p v-if="paymentInfo.external_reference">
          <strong>Número de Pedido:</strong> #{{ paymentInfo.external_reference }}
        </p>
      </div>

      <div class="destacado">
        <svg viewBox="0 0 24 24" class="icono-chico">
          <path fill="currentColor" d="M12 2C6.5 2 2 6.5 2 12s4.5 10 10 10 10-4.5 10-10S17.5 2 12 2m-1 15-5-5 1.41-1.41L11 13.17l6.59-6.58L19 8l-8 9Z"/>
        </svg>
        <div>
          <h3>¡Pago completado con éxito!</h3>
          <p>Tu transacción fue aprobada y registrada correctamente.</p>
          <p class="nota">Recibirás la confirmación y el comprobante por correo electrónico.</p>
        </div>
      </div>

      <div class="siguiente-box">
        <h3>¿Qué podés hacer ahora?</h3>
        <ul>
          <li>Ver el detalle de tus compras recientes</li>
          <li>Descargar tu comprobante si lo deseás</li>
          <li>Seguir explorando el catálogo</li>
        </ul>
      </div>

      <div class="acciones">
        <button class="btn-principal" @click="irAMisCompras"><Icono name="caja" /> Ver Mis Compras</button>
        <button class="btn-secundario" @click="irAHome"><Icono name="llave" /> Volver al Inicio</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import Icono from '../components/Icono.vue'
import api from '../services/api'

const router = useRouter()
const route = useRoute()
const paymentInfo = ref(null)

onMounted(async () => {
  paymentInfo.value = {
    payment_id: route.query.payment_id,
    status: route.query.status,
    external_reference: route.query.external_reference,
    merchant_order_id: route.query.merchant_order_id
  }

  if (paymentInfo.value.payment_id) {
    try {
      await api.post(`/pagos/public/procesar-resultado/${paymentInfo.value.payment_id}`)
    } catch (err) {
      console.error('No se pudo confirmar el pago con el backend:', err)
    }
  }
})

const irAMisCompras = () => router.push('/mis-compras')
const irAHome = () => router.push('/')
</script>

<style scoped>
.pago-page {
  min-height: 80vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  background: var(--wf-bg);
  font-family: var(--wf-font-body);
  color: var(--wf-text);
  --status: var(--wf-success);
}

.pago-card {
  background: var(--wf-surface);
  border: 1px solid var(--wf-border);
  border-radius: 20px;
  padding: 40px;
  max-width: 500px;
  width: 100%;
  text-align: center;
}

.icono-wrap {
  margin-bottom: 24px;
  position: relative;
  display: inline-block;
}

.icono {
  width: 80px;
  height: 80px;
  color: var(--status);
  animation: scaleIn 0.6s ease-out;
}

.halo {
  position: absolute;
  top: 50%;
  left: 50%;
  width: 100px;
  height: 100px;
  margin: -50px 0 0 -50px;
  border: 4px solid var(--wf-border);
  border-top: 4px solid var(--status);
  border-radius: 50%;
  animation: spin 2s linear infinite;
}

h1 {
  font-size: 26px;
  margin-bottom: 12px;
  color: var(--wf-text);
}

.subtexto {
  font-size: 16px;
  color: var(--wf-text-muted);
  margin-bottom: 20px;
}

.info-box {
  background: var(--wf-bg-deep);
  border-radius: 10px;
  padding: 16px;
  margin-bottom: 24px;
  text-align: left;
}
.info-box p {
  margin: 4px 0;
}

.destacado {
  background: var(--wf-bg-deep);
  border-left: 4px solid var(--status);
  border-radius: 10px;
  padding: 20px;
  margin-bottom: 24px;
  display: flex;
  gap: 12px;
  text-align: left;
}

.icono-chico {
  width: 28px;
  height: 28px;
  color: var(--status);
  flex-shrink: 0;
}

.destacado h3 {
  font-size: 16px;
  color: var(--wf-text);
  margin-bottom: 6px;
}
.destacado p {
  font-size: 14px;
  color: var(--wf-text-muted);
  margin: 4px 0;
}
.destacado .nota {
  font-size: 12px;
  font-style: italic;
  margin-top: 6px;
}

.siguiente-box {
  background: var(--wf-bg-deep);
  border-radius: 10px;
  padding: 18px;
  text-align: left;
  margin-bottom: 24px;
}
.siguiente-box h3 {
  font-size: 16px;
  color: var(--wf-text);
  margin-bottom: 8px;
}
.siguiente-box ul {
  list-style: none;
  padding: 0;
  margin: 0;
  color: var(--wf-text-muted);
}
.siguiente-box li {
  padding: 4px 0;
}
.siguiente-box li::before {
  content: '✓ ';
  color: var(--status);
}

.acciones {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.btn-principal {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 12px;
  border: none;
  border-radius: 10px;
  background: var(--status);
  color: #0E1B14;
  font-size: 16px;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.3s;
}
.btn-principal:hover {
  filter: brightness(1.1);
  transform: scale(1.02);
}

.btn-secundario {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 12px;
  border: 1px solid var(--wf-border);
  border-radius: 10px;
  background: transparent;
  color: var(--wf-text);
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
}
.btn-secundario:hover {
  background: var(--wf-bg-deep);
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}
@keyframes scaleIn {
  from { transform: scale(0); opacity: 0; }
  to { transform: scale(1); opacity: 1; }
}

@media (max-width: 480px) {
  .pago-card {
    padding: 22px;
  }
}
</style>
