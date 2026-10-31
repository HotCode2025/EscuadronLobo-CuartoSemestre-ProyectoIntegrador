<template>
  <div class="pago-page pending">
    <div class="pago-card">
      <div class="icono-wrap">
        <svg viewBox="0 0 24 24" class="icono">
          <path
            fill="currentColor"
            d="M12 2C6.5 2 2 6.5 2 12S6.5 22 12 22 22 17.5 22 12 17.5 2 12 2M12 20C7.59 20 4 16.41 4 12S7.59 4 12 4 20 7.59 20 12 16.41 20 12 20M16.59 7.58L10 14.17L7.41 11.59L6 13L10 17L18 9L16.59 7.58Z"
          />
        </svg>
        <div class="halo"></div>
      </div>

      <h1><Icono name="reloj" /> Pago Pendiente</h1>
      <p class="subtexto">Tu pago está siendo procesado.</p>

      <div v-if="paymentInfo" class="info-box">
        <p v-if="paymentInfo.payment_id"><strong>ID de Pago:</strong> {{ paymentInfo.payment_id }}</p>
        <p><strong>Estado:</strong> {{ paymentInfo.status || 'pending' }}</p>
        <p v-if="paymentInfo.external_reference">
          <strong>Número de Pedido:</strong> #{{ paymentInfo.external_reference }}
        </p>
      </div>

      <div class="destacado">
        <svg viewBox="0 0 24 24" class="icono-chico">
          <path fill="currentColor" d="M13 14H11V9H13M13 18H11V16H13M1 21H23L12 2L1 21Z"/>
        </svg>
        <div>
          <h3>¿Qué significa esto?</h3>
          <p>Tu pago está siendo verificado. Te notificaremos por email cuando se complete.</p>
          <p class="nota">Esto puede tardar entre unos minutos y 48 horas hábiles.</p>
        </div>
      </div>

      <div class="siguiente-box">
        <h3>Motivos comunes de pago pendiente:</h3>
        <ul>
          <li>Pago en efectivo (Rapipago, Pago Fácil, etc.)</li>
          <li>Transferencia bancaria en proceso</li>
          <li>Verificación adicional del banco</li>
          <li>Pago con saldo de Mercado Pago</li>
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
  --status: var(--wf-accent);
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
  animation: spin 1.6s linear infinite;
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
  content: '• ';
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
  color: #161616;
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
