<template>
  <footer class="footer">
    <div class="marcas-carrusel">
      <div class="marcas-track">
        <div
          v-for="(marca, i) in marcasLoop"
          :key="i"
          class="marca-card"
          :class="{ 'marca-gamma': marca.nombre === 'Gamma' }"
        >
          <img :src="marca.logo" :alt="marca.nombre" />
        </div>
      </div>
    </div>

    <div class="beneficios-bar">
      <div class="beneficio-item">
        <svg class="beneficio-icono" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <rect x="1" y="7" width="14" height="10" rx="1.5"></rect>
          <path d="M15 10h3.5a1.5 1.5 0 0 1 1.3.76L22 14.5V17a1 1 0 0 1-1 1h-1"></path>
          <circle cx="6.5" cy="19" r="1.8"></circle>
          <circle cx="17.5" cy="19" r="1.8"></circle>
        </svg>
        <div class="beneficio-texto">
          <span class="beneficio-titulo">Envíos a todo el país</span>
          <span class="beneficio-subtexto">Entrega puerta a puerta</span>
        </div>
      </div>
      <div class="beneficio-item">
        <svg class="beneficio-icono" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <path d="M12 2.5 4 5.5v6c0 5 3.4 8.7 8 10 4.6-1.3 8-5 8-10v-6Z"></path>
          <path d="m9 12 2 2 4-4.5"></path>
        </svg>
        <div class="beneficio-texto">
          <span class="beneficio-titulo">Garantía de reembolso</span>
          <span class="beneficio-subtexto">100% devolución del dinero</span>
        </div>
      </div>
      <div class="beneficio-item">
        <svg class="beneficio-icono" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <rect x="4" y="10.5" width="16" height="11" rx="1.5"></rect>
          <path d="M8 10.5V7a4 4 0 0 1 8 0v3.5"></path>
          <circle cx="12" cy="15.5" r="1.6"></circle>
        </svg>
        <div class="beneficio-texto">
          <span class="beneficio-titulo">Pago seguro</span>
          <span class="beneficio-subtexto">Con Mercado Pago</span>
        </div>
      </div>
    </div>

    <div class="footer-links">
      <div class="footer-col">
        <h4>Atención al cliente</h4>
        <router-link to="/contacto" class="link-footer">Contacto</router-link>
        <router-link to="/preguntas-frecuentes" class="link-footer">Preguntas frecuentes</router-link>
        <router-link to="/mis-compras" class="link-footer">Seguimiento de pedidos</router-link>
      </div>
      <div class="footer-col">
        <h4>Información</h4>
        <router-link to="/envios-y-entregas" class="link-footer">Envíos y entregas</router-link>
        <router-link to="/cambios-y-devoluciones" class="link-footer">Cambios y devoluciones</router-link>
        <router-link to="/garantias" class="link-footer">Garantías</router-link>
      </div>
      <div class="footer-col">
        <h4>Nuestra tienda</h4>
        <router-link to="/quienes-somos" class="link-footer">Quiénes somos</router-link>
        <router-link to="/" class="link-footer">Marcas</router-link>
      </div>
      <div class="footer-col">
        <h4>Medios de pago</h4>
        <div class="footer-pagos">
          <a href="https://www.mercadopago.com.ar" target="_blank" rel="noopener" class="badge-mp">
            <img src="/branding/mercadopago.svg" alt="Mercado Pago" />
          </a>
        </div>
      </div>
      <div class="footer-col">
        <h4>Seguinos</h4>
        <div class="footer-social">
          <a href="https://instagram.com" target="_blank" rel="noopener" aria-label="Instagram">
            <Icono name="instagram" />
          </a>
          <a href="https://facebook.com" target="_blank" rel="noopener" aria-label="Facebook">
            <Icono name="facebook" />
          </a>
          <a href="https://x.com" target="_blank" rel="noopener" aria-label="X">
            <Icono name="x" />
          </a>
        </div>
      </div>
    </div>

    <div class="footer-legal">
      <router-link to="/privacidad" class="link-activo">Privacidad</router-link> · <router-link to="/terminos-y-condiciones" class="link-activo">Términos y condiciones</router-link>
    </div>

    <p class="footer-copy">© {{ anioActual }} WolfForge · Escuadrón Lobo</p>
  </footer>
</template>

<script setup>
import Icono from './Icono.vue'

const anioActual = new Date().getFullYear()

const marcas = [
  { nombre: 'Bosch', logo: '/branding/marcas/bosch.svg' },
  { nombre: 'DeWalt', logo: '/branding/marcas/dewalt.svg' },
  { nombre: 'Makita', logo: '/branding/marcas/makita.svg' },
  { nombre: 'Gamma', logo: '/branding/marcas/gamma.png' },
  { nombre: 'Lüsqtoff', logo: '/branding/marcas/lusqtoff.png' },
  { nombre: 'Lincoln', logo: '/branding/marcas/lincoln.svg' }
]

// Repetido varias veces para que siempre haya de sobra, incluso en pantallas anchas,
// y el loop de la animación sea continuo sin salto visible
const marcasLoop = [...marcas, ...marcas, ...marcas, ...marcas]
</script>

<style scoped>
.footer {
  background: var(--wf-bg-deep);
  border-top: 1px solid var(--wf-border);
  padding: 24px 28px;
  text-align: center;
  font-family: var(--wf-font-body);
}

.marcas-carrusel {
  overflow: hidden;
  margin: -24px -28px 20px;
  padding: 18px 28px;
  background: var(--wf-accent);
  -webkit-mask-image: linear-gradient(to right, transparent, #000 13%, #000 87%, transparent);
  mask-image: linear-gradient(to right, transparent, #000 13%, #000 87%, transparent);
}

.marcas-track {
  display: flex;
  gap: 14px;
  width: max-content;
  animation: desplazar-marcas 22s linear infinite;
}

@keyframes desplazar-marcas {
  from { transform: translateX(0); }
  to { transform: translateX(-25%); }
}

.marca-card {
  flex: 0 0 140px;
  height: 64px;
  background: #fff;
  border-radius: 10px;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 10px 16px;
  transition: transform 0.2s ease;
}

.marca-card:hover {
  transform: scale(1.1);
}

.marca-card img {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
}

.marca-card.marca-gamma img {
  max-width: 175%;
  max-height: 175%;
}

.beneficios-bar {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 48px;
  padding: 10px 0 28px;
  margin-bottom: 18px;
  border-bottom: 1px solid var(--wf-border);
}

.beneficio-item {
  display: flex;
  align-items: center;
  gap: 14px;
}

.beneficio-icono {
  width: 42px;
  height: 42px;
  flex: none;
  color: var(--wf-accent);
}

.beneficio-texto {
  display: flex;
  flex-direction: column;
  text-align: left;
}

.beneficio-titulo {
  color: var(--wf-text);
  font-weight: 700;
  font-size: 1.08em;
}

.beneficio-subtexto {
  color: var(--wf-text-muted);
  font-size: 0.92em;
}

.footer-pagos {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  justify-content: flex-start;
  margin-top: 10px;
}

.footer-texto {
  color: var(--wf-text-muted);
  font-size: 0.85em;
}

.badge-mp {
  display: inline-flex;
  align-items: center;
  background: #fff;
  padding: 10px 18px;
  border-radius: 10px;
  cursor: pointer;
  transition: transform 0.2s ease;
}

.badge-mp:hover {
  transform: scale(1.04);
}

.badge-mp img {
  height: 34px;
  width: auto;
  display: block;
}

.footer-copy {
  color: var(--wf-text-muted);
  font-size: 0.8em;
  margin: 0;
}

.footer-links {
  display: grid;
  grid-template-columns: repeat(5, minmax(120px, 1fr));
  gap: 28px;
  max-width: 900px;
  margin: 0 auto 28px;
  padding-top: 28px;
  border-top: 1px solid var(--wf-border);
  text-align: left;
}

.footer-col h4 {
  position: relative;
  display: inline-block;
  color: var(--wf-accent);
  font-size: 0.95em;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  margin: 0 0 16px;
  padding-bottom: 8px;
}

.footer-col h4::after {
  content: '';
  position: absolute;
  left: 0;
  bottom: 0;
  width: 26px;
  height: 2px;
  background: var(--wf-accent);
  border-radius: 2px;
}

.footer-col .link-inactivo {
  display: block;
  color: var(--wf-text-muted);
  font-size: 0.9em;
  margin-bottom: 10px;
  cursor: default;
  transition: color 0.2s ease;
}

.footer-col .link-footer {
  display: block;
  color: var(--wf-text-muted);
  font-size: 0.9em;
  margin-bottom: 10px;
  text-decoration: none;
  transition: color 0.2s ease;
}

.footer-col .link-footer:hover {
  color: var(--wf-accent);
  text-decoration: underline;
}

.footer-social {
  display: flex;
  gap: 12px;
}

.footer-social a {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  border-radius: 50%;
  border: 1px solid var(--wf-border);
  color: var(--wf-text);
  transition: all 0.2s ease;
}

.footer-social a svg {
  width: 18px;
  height: 18px;
}

.footer-social a:hover {
  background: var(--wf-accent);
  color: #161616;
  border-color: var(--wf-accent);
}

.footer-legal {
  text-align: center;
  color: var(--wf-text-muted);
  font-size: 0.85em;
  margin-bottom: 10px;
}

.footer-legal .link-inactivo {
  cursor: default;
}

.link-activo {
  color: var(--wf-text-muted);
  text-decoration: none;
  transition: color 0.2s ease;
}

.link-activo:hover {
  color: var(--wf-accent);
  text-decoration: underline;
}

@media (max-width: 600px) {
  .footer-links {
    grid-template-columns: repeat(2, 1fr);
    text-align: center;
  }

  /* Reordeno visualmente (sin tocar el orden real del DOM, así el desktop no cambia):
     "Seguinos" pasa a emparejarse con "Nuestra tienda" (altura parecida),
     y "Medios de pago" queda solo, centrado, en su propia fila abajo. */
  .footer-col:nth-child(4) {
    order: 5;
    grid-column: 1 / -1;
    text-align: center;
  }

  .footer-col:nth-child(5) {
    order: 4;
  }

  .footer-social {
    justify-content: center;
  }

  .footer-pagos {
    justify-content: center;
  }
}

@media (max-width: 600px) {
  .footer {
    padding: 18px 16px;
  }

  .marcas-carrusel {
    margin: -18px -16px 16px;
    padding: 14px 16px;
  }

  .marca-card {
    flex: 0 0 110px;
    height: 50px;
  }

  .beneficios-bar {
    gap: 20px;
    justify-content: flex-start;
    text-align: left;
  }

  .beneficio-item:nth-child(3) {
    flex: 1 1 100%;
    justify-content: center;
  }

  .beneficio-icono {
    width: 32px;
    height: 32px;
  }

  .beneficio-titulo {
    font-size: 0.95em;
  }

  .beneficio-subtexto {
    font-size: 0.8em;
  }
}
</style>
