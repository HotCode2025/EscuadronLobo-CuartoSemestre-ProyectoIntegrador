import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const routes = [
  { path: '/', component: () => import('../views/Home.vue'), meta: { title: 'Catálogo' } },
  { path: '/herramienta/:id', component: () => import('../views/HerramientaDetalle.vue'), meta: { title: 'Herramienta' } },
  { path: '/login', component: () => import('../views/Login.vue'), meta: { title: 'Ingresar' } },
  { path: '/register', component: () => import('../views/Register.vue'), meta: { title: 'Registrarse' } },
  { path: '/carrito', component: () => import('../views/Carrito.vue'), meta: { requiresAuth: true, title: 'Carrito' } },
  { path: '/admin', component: () => import('../views/AdminDashboard.vue'), meta: { requiresAuth: true, role: 'ADMINISTRADOR', title: 'Panel de administración' } },
  {
    path: '/mis-compras',
    component: () => import('../views/MisCompras.vue'),
    meta: { requiresAuth: true, title: 'Mis Compras' }
  },
  {
    path: '/admin/herramientas',
    component: () => import('../views/AdminHerramientas.vue'),
    meta: { requiresAuth: true, role: 'ADMINISTRADOR', title: 'Administrar Herramientas' }
  },
  // Rutas de resultado de pago de Mercado Pago
  {
    path: '/pago/success',
    component: () => import('../views/PagoSuccess.vue'),
    meta: { title: 'Pago aprobado' }
  },
  {
    path: '/pago/failure',
    component: () => import('../views/PagoFailure.vue'),
    meta: { title: 'Pago rechazado' }
  },
  {
    path: '/pago/pending',
    component: () => import('../views/PagoPending.vue'),
    meta: { title: 'Pago pendiente' }
  },
  {
    path: '/terminos-y-condiciones',
    component: () => import('../views/TerminosCondiciones.vue'),
    meta: { title: 'Términos y Condiciones' }
  },
  {
    path: '/contacto',
    component: () => import('../views/Contacto.vue'),
    meta: { title: 'Contacto' }
  },
  {
    path: '/preguntas-frecuentes',
    component: () => import('../views/PreguntasFrecuentes.vue'),
    meta: { title: 'Preguntas Frecuentes' }
  },
  {
    path: '/envios-y-entregas',
    component: () => import('../views/EnviosEntregas.vue'),
    meta: { title: 'Envíos y Entregas' }
  },
  {
    path: '/cambios-y-devoluciones',
    component: () => import('../views/CambiosDevoluciones.vue'),
    meta: { title: 'Cambios y Devoluciones' }
  },
  {
    path: '/garantias',
    component: () => import('../views/Garantias.vue'),
    meta: { title: 'Garantías' }
  },
  {
    path: '/privacidad',
    component: () => import('../views/Privacidad.vue'),
    meta: { title: 'Política de Privacidad' }
  },
  {
    path: '/quienes-somos',
    component: () => import('../views/QuienesSomos.vue'),
    meta: { title: 'Quiénes Somos' }
  },
  {
    path: '/:pathMatch(.*)*',
    component: () => import('../views/NotFound.vue'),
    meta: { title: 'Página no encontrada' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) return savedPosition
    if (to.path === from.path) return false
    return { top: 0 }
  }
})

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()
  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    next('/login')
  } else if (to.meta.role && authStore.user?.rol !== to.meta.role) {
    next('/')
  } else {
    next()
  }
})

router.afterEach((to) => {
  document.title = to.meta.title ? `${to.meta.title} · WolfForge` : 'WolfForge'
})

export default router