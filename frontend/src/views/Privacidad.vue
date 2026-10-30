<template>
  <div class="pagina-info">
    <h1><Icono name="ojo" /> Política de Privacidad</h1>
    <section class="bloque">
      <h2>Datos que recopilamos</h2>
      <p>
        Al registrarte, guardamos tu nombre, email, teléfono y dirección — solo lo
        necesario para gestionar tu cuenta y tus pedidos.
      </p>
    </section>
    <section class="bloque">
      <h2>Uso de la información</h2>
      <p>
        Usamos tus datos únicamente para procesar pedidos, enviarte confirmaciones y
        mejorar el servicio. No compartimos tu información con terceros, salvo lo
        necesario para procesar el pago a través de Mercado Pago.
      </p>
    </section>
    <section class="bloque">
      <h2>Contraseñas</h2>
      <p>
        Tu contraseña se guarda siempre encriptada — nunca almacenamos ni podemos ver tu
        contraseña en texto plano.
      </p>
    </section>
    <section class="bloque">
      <h2>Tus derechos</h2>
      <p>
        Podés solicitar la eliminación de tu cuenta y tus datos en cualquier momento
        escribiéndonos a través de la sección de Contacto.
      </p>
    </section>
    <router-link to="/" class="volver">← Volver al catálogo</router-link>
  </div>
</template>

<script setup>
import Icono from '../components/Icono.vue'
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
