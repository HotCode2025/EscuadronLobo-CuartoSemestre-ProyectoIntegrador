import { defineStore } from 'pinia'

export const useCarritoStore = defineStore('carrito', {
  state: () => ({
    items: JSON.parse(localStorage.getItem('carrito') || '[]')
  }),
  getters: {
    total: (state) => state.items.reduce((sum, item) => sum + (item.precio * item.cantidad), 0),
    cantidadTotal: (state) => state.items.reduce((sum, item) => sum + item.cantidad, 0)
  },
  actions: {
    agregar(herramienta, cantidad = 1) {
      const existente = this.items.find(i => i.idHerramienta === herramienta.idHerramienta)
      if (existente) {
        existente.cantidad += cantidad
      } else {
        // Solo guardar campos necesarios, NO el stock
        this.items.push({
          idHerramienta: herramienta.idHerramienta,
          nombre: herramienta.nombre,
          marca: herramienta.marca,
          precio: herramienta.precio,
          imagenUrl: herramienta.imagenUrl,
          cantidad
        })
      }
      localStorage.setItem('carrito', JSON.stringify(this.items))
    },
    incrementar(idHerramienta) {
      const item = this.items.find(i => i.idHerramienta === idHerramienta)
      if (item) {
        item.cantidad++
        localStorage.setItem('carrito', JSON.stringify(this.items))
      }
    },
    decrementar(idHerramienta) {
      const item = this.items.find(i => i.idHerramienta === idHerramienta)
      if (item && item.cantidad > 1) {
        item.cantidad--
        localStorage.setItem('carrito', JSON.stringify(this.items))
      }
    },
    eliminar(idHerramienta) {
      this.items = this.items.filter(i => i.idHerramienta !== idHerramienta)
      localStorage.setItem('carrito', JSON.stringify(this.items))
    },
    limpiar() {
      this.items = []
      localStorage.removeItem('carrito')
    }
  }
})