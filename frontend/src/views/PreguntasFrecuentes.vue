<template>
  <div class="pagina-info">
    <h1><Icono name="tuerca" /> Preguntas Frecuentes</h1>

    <section class="bloque" v-for="(item, i) in preguntas" :key="i">
      <h2>{{ item.pregunta }}</h2>
      <p>{{ item.respuesta }}</p>
    </section>

    <router-link to="/" class="volver">← Volver al catálogo</router-link>
  </div>
</template>

<script setup>
import Icono from '../components/Icono.vue'

const preguntas = [
  { pregunta: '¿Qué medios de pago aceptan?', respuesta: 'Aceptamos todos los medios de pago disponibles a través de Mercado Pago: tarjetas de crédito, débito y dinero en cuenta.' },
  { pregunta: '¿Cómo sé si mi pedido fue confirmado?', respuesta: 'Una vez aprobado el pago, el pedido pasa automáticamente a estado "Confirmado". Podés verlo en cualquier momento desde "Mis Compras".' },
  { pregunta: '¿Puedo cancelar un pedido?', respuesta: 'Sí, mientras el pedido esté en estado "Pendiente" o "Confirmado" podés cancelarlo desde tu cuenta.' },
  { pregunta: '¿Cómo pido una devolución?', respuesta: 'Una vez que recibiste el producto, podés solicitar la devolución desde "Mis Compras". El equipo de WolfForge revisa la solicitud.' },
  { pregunta: '¿Necesito crear una cuenta para comprar?', respuesta: 'Sí, es necesario registrarte para poder realizar un pedido y hacer seguimiento del mismo.' }
]
</script>

<style scoped>
.pagina-info {
  max-width: 720px;
  margin: 16px auto 40px;
  padding: 30px;
  background: var(--wf-bg);
  font-family: var(--wf-font-body);
  color: var(--wf-text);
}
.pagina-info h1 {
  text-align: center;
  font-size: 1.8em;
  margin-bottom: 30px;
}
.bloque {
  background: var(--wf-surface);
  border: 1px solid var(--wf-border);
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 16px;
}
.bloque h2 {
  font-size: 1.1em;
  color: var(--wf-accent);
  margin: 0 0 8px;
}
.bloque p {
  margin: 0;
  color: var(--wf-text-muted);
  line-height: 1.6;
}
.volver {
  display: inline-block;
  margin-top: 10px;
  color: var(--wf-accent);
  text-decoration: none;
  font-weight: 600;
}
.volver:hover {
  text-decoration: underline;
}
</style>
