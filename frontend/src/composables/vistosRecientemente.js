import { ref } from 'vue'

const CLAVE = 'wolfforge_vistos_recientemente'
const MAX_ITEMS = 8

function leer() {
  try {
    return JSON.parse(localStorage.getItem(CLAVE) || '[]')
  } catch {
    return []
  }
}

export const vistosRecientemente = ref(leer())

export function registrarVisto(herramienta) {
  if (!herramienta?.idHerramienta) return

  const actuales = vistosRecientemente.value.filter(
    h => h.idHerramienta !== herramienta.idHerramienta
  )

  actuales.unshift({
    idHerramienta: herramienta.idHerramienta,
    nombre: herramienta.nombre,
    marca: herramienta.marca,
    precio: herramienta.precio,
    imagenUrl: herramienta.imagenUrl
  })

  vistosRecientemente.value = actuales.slice(0, MAX_ITEMS)
  localStorage.setItem(CLAVE, JSON.stringify(vistosRecientemente.value))
}
