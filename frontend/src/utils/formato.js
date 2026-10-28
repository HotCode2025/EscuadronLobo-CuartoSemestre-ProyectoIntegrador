export function formatearPrecio(valor) {
  const numero = Number(valor) || 0
  return numero.toLocaleString('es-AR', { maximumFractionDigits: 0 })
}
