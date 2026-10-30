<template>
  <div class="terminos-container">
    <h1><Icono name="papelera" /> Términos y Condiciones</h1>
    <p class="actualizado">Última actualización: {{ fechaActual }}</p>

    <section class="bloque">
      <h2>1. Aceptación de los términos</h2>
      <p>
        Al acceder y utilizar WolfForge, aceptás los presentes Términos y Condiciones.
        Si no estás de acuerdo con alguno de los puntos aquí descritos, te pedimos que
        no utilices el sitio.
      </p>
    </section>

    <section class="bloque">
      <h2>2. Productos y precios</h2>
      <p>
        Los precios publicados están expresados en pesos argentinos e incluyen los
        impuestos correspondientes. WolfForge se reserva el derecho de modificar precios,
        descripciones o disponibilidad de stock sin aviso previo. Las imágenes son
        ilustrativas y pueden no coincidir exactamente con el producto final.
      </p>
    </section>

    <section class="bloque">
      <h2>3. Cuentas de usuario</h2>
      <p>
        Para realizar una compra es necesario registrarse con datos reales y vigentes.
        Sos responsable de mantener la confidencialidad de tu contraseña y de toda
        actividad realizada desde tu cuenta.
      </p>
    </section>

    <section class="bloque">
      <h2>4. Medios de pago</h2>
      <p>
        Los pagos se procesan a través de <strong>Mercado Pago</strong>. WolfForge no
        almacena datos de tarjetas ni información financiera sensible — todo el
        procesamiento del pago ocurre en los servidores de Mercado Pago. Un pedido se
        considera confirmado recién cuando el pago es aprobado.
      </p>
    </section>

    <section class="bloque">
      <h2>5. Cancelaciones y devoluciones</h2>
      <p>
        Un pedido puede cancelarse mientras se encuentre en estado "Pendiente" o
        "Confirmado". Una vez recibido el producto, el usuario puede solicitar una
        devolución desde la sección "Mis Compras"; la solicitud queda sujeta a revisión
        por parte del equipo de WolfForge.
      </p>
    </section>

    <section class="bloque">
      <h2>6. Responsabilidad</h2>
      <p>
        WolfForge no se responsabiliza por el uso indebido de las herramientas
        adquiridas. Cada producto debe utilizarse siguiendo las indicaciones del
        fabricante correspondiente.
      </p>
    </section>

    <section class="bloque">
      <h2>7. Modificaciones</h2>
      <p>
        Estos Términos y Condiciones pueden actualizarse en cualquier momento. El uso
        continuado del sitio luego de una modificación implica la aceptación de los
        nuevos términos.
      </p>
    </section>

    <router-link to="/" class="volver">← Volver al catálogo</router-link>
  </div>
</template>

<script setup>
import Icono from '../components/Icono.vue'

const fechaActual = new Date().toLocaleDateString('es-AR', { year: 'numeric', month: 'long', day: 'numeric' })
</script>

<style scoped>
.terminos-container {
  max-width: 720px;
  margin: 16px auto 40px;
  padding: 30px;
  background: var(--wf-bg);
  font-family: var(--wf-font-body);
  color: var(--wf-text);
}

.terminos-container h1 {
  text-align: center;
  font-size: 1.8em;
  margin-bottom: 6px;
}

.actualizado {
  text-align: center;
  color: var(--wf-text-muted);
  font-size: 0.85em;
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
