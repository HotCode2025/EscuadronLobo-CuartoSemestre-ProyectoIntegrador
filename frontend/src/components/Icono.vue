<template>
  <svg
    class="icono"
    viewBox="0 0 24 24"
    fill="none"
    stroke="currentColor"
    stroke-width="2"
    stroke-linecap="round"
    stroke-linejoin="round"
  >
    <template v-if="name === 'llave'">
      <path d="M19.4 7.3a4.5 4.5 0 0 1-5.9 5.9l-6.2 6.2a2 2 0 1 1-2.7-2.7l6.2-6.2a4.5 4.5 0 0 1 5.9-5.9l-2.4 2.4a1 1 0 0 0 0 1.4l2 2a1 1 0 0 0 1.4 0Z" />
    </template>

    <template v-else-if="name === 'carrito'">
      <circle cx="9" cy="20" r="1.4" />
      <circle cx="18" cy="20" r="1.4" />
      <path d="M2.5 3h2l2.3 11.4a2 2 0 0 0 2 1.6h8.4a2 2 0 0 0 2-1.6L21 7H6" />
    </template>

    <template v-else-if="name === 'tuerca'">
      <path d="M12 2 20 7v10l-8 5-8-5V7Z" />
      <circle cx="12" cy="12" r="3" />
    </template>

    <template v-else-if="name === 'check-circulo'">
      <circle cx="12" cy="12" r="9" />
      <path d="m8 12 3 3 5-6" />
    </template>

    <template v-else-if="name === 'x-circulo'">
      <circle cx="12" cy="12" r="9" />
      <path d="M9 9l6 6M15 9l-6 6" />
    </template>

    <template v-else-if="name === 'reloj'">
      <circle cx="12" cy="12" r="9" />
      <path d="M12 7v5l3.5 2" />
    </template>

    <template v-else-if="name === 'caja'">
      <path d="M3 7.5 12 3l9 4.5v9L12 21l-9-4.5Z" />
      <path d="M3 7.5 12 12l9-4.5M12 12v9" />
    </template>

    <template v-else-if="name === 'lapiz'">
      <path d="M4 20h4L18.5 9.5a2.1 2.1 0 1 0-3-3L5 17v3Z" />
      <path d="M14 6l4 4" />
    </template>

    <template v-else-if="name === 'papelera'">
      <path d="M4 7h16" />
      <path d="M10 11v6M14 11v6" />
      <path d="M6 7l1 13a2 2 0 0 0 2 2h6a2 2 0 0 0 2-2l1-13" />
      <path d="M9 7V4a1 1 0 0 1 1-1h4a1 1 0 0 1 1 1v3" />
    </template>

    <template v-else-if="name === 'carpeta'">
      <path d="M3 6a1 1 0 0 1 1-1h5l2 2h9a1 1 0 0 1 1 1v10a1 1 0 0 1-1 1H4a1 1 0 0 1-1-1Z" />
    </template>

    <template v-else-if="name === 'etiqueta'">
      <path d="M12.6 2.6 21 11l-9 9-8.4-8.4V3h8.4Z" />
      <circle cx="7" cy="7" r="1.3" />
    </template>

    <template v-else-if="name === 'ojo'">
      <path d="M2 12s3.8-7 10-7 10 7 10 7-3.8 7-10 7-10-7-10-7Z" />
      <circle cx="12" cy="12" r="3" />
    </template>

    <template v-else-if="name === 'fuego'">
      <path d="M12 2s-5 5.5-5 10a5 5 0 0 0 10 0c0-1.5-.8-2.5-1.5-3.5.3 1.3-.5 2.3-1.3 2.3-1 0-1.2-1-1-2C13.6 7 12 4.5 12 2Z" />
    </template>

    <template v-else-if="name === 'tarjeta'">
      <rect x="2.5" y="5.5" width="19" height="13" rx="1.5" />
      <path d="M2.5 9.5h19" />
    </template>

    <template v-else-if="name === 'reintentar'">
      <path d="M3 12a9 9 0 0 1 15-6.7" />
      <path d="M21 12a9 9 0 0 1-15 6.7" />
      <path d="M18 3v4h-4" />
      <path d="M6 21v-4h4" />
    </template>

    <template v-else-if="name === 'engranaje'">
      <circle cx="12" cy="12" r="3" />
      <path d="M12 2v3M12 19v3M4.2 4.2l2.1 2.1M17.7 17.7l2.1 2.1M2 12h3M19 12h3M4.2 19.8l2.1-2.1M17.7 6.3l2.1-2.1" />
    </template>

    <template v-else-if="name === 'menu'">
      <path d="M3 6h18M3 12h18M3 18h18" />
    </template>

    <template v-else-if="name === 'instagram'">
      <rect x="2" y="2" width="20" height="20" rx="5" ry="5" />
      <path d="M16 11.37A4 4 0 1 1 12.63 8 4 4 0 0 1 16 11.37z" />
      <line x1="17.5" y1="6.5" x2="17.51" y2="6.5" />
    </template>

    <template v-else-if="name === 'facebook'">
      <path d="M18 2h-3a5 5 0 0 0-5 5v3H7v4h3v8h4v-8h3l1-4h-4V7a1 1 0 0 1 1-1h3z" />
    </template>

    <template v-else-if="name === 'x'">
      <path
        fill="currentColor"
        stroke="none"
        d="M18.9 1.15h3.68l-8.04 9.19L24 22.85h-7.41l-5.8-7.58-6.64 7.58H.47l8.6-9.83L0 1.15h7.59l5.24 6.93zm-1.61 19.3h2.04L6.49 3.24H4.3z"
      />
    </template>
  </svg>
</template>

<script setup>
defineProps({
  name: { type: String, required: true }
})
</script>

<style scoped>
.icono {
  width: 1em;
  height: 1em;
  display: inline-block;
  vertical-align: -0.15em;
  flex: none;
}
</style>
