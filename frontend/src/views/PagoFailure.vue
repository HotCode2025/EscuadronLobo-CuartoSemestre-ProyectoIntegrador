<template>
  <div class="pago-page failure">
    <div class="pago-card">
      <div class="icono-wrap">
        <svg viewBox="0 0 24 24" class="icono">
          <path
            fill="currentColor"
            d="M12 2C6.5 2 2 6.5 2 12S6.5 22 12 22 22 17.5 22 12 17.5 2 12 2M12 20C7.59 20 4 16.41 4 12S7.59 4 12 4 20 7.59 20 12 16.41 20 12 20M15.59 7L12 10.59L8.41 7L7 8.41L10.59 12L7 15.59L8.41 17L12 13.41L15.59 17L17 15.59L13.41 12L17 8.41L15.59 7Z"
          />
        </svg>
      </div>

      <h1><Icono name="x-circulo" /> Pago Rechazado</h1>
      <p class="subtexto">Lo sentimos, tu pago no pudo ser procesado.</p>

      <div v-if="paymentInfo" class="info-box">
        <p v-if="paymentInfo.payment_id"><strong>ID de Pago:</strong> {{ paymentInfo.payment_id }}</p>
        <p><strong>Estado:</strong> {{ paymentInfo.status || 'rejected' }}</p>
        <p v-if="paymentInfo.external_reference">
          <strong>Número de Pedido:</strong> #{{ paymentInfo.external_reference }}
        </p>
      </div>

      <div class="siguiente-box">
        <h3>Posibles causas:</h3>
        <ul>
          <li>Fondos insuficientes</li>
          <li>Datos de tarjeta incorrectos</li>
          <li>La tarjeta está vencida</li>
          <li>El pago fue rechazado por el banco</li>
        </ul>
      </div>

      <div class="acciones">
        <button class="btn-principal" @click="intentarNuevamente"><Icono name="reintentar" /> Intentar Nuevamente</button>
        <button class="btn-secundario" @click="irAHome"><Icono name="llave" /> Volver al Inicio</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import Icono from '../components/Icono.vue'

const router = useRouter()
const route = useRoute()
const paymentInfo = ref(null)

onMounted(() => {
  paymentInfo.value = {
    payment_id: route.query.payment_id,
    status: route.query.status,
    external_reference: route.query.external_reference,
    merchant_order_id: route.query.merchant_order_id
  }
})

const intentarNuevamente = () => router.push('/carrito')
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
  --status: var(--wf-accent-2);
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
}

.icono {
  width: 80px;
  height: 80px;
  color: var(--status);
  animation: scaleIn 0.6s ease-out;
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
  content: '✕ ';
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
  color: white;
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
