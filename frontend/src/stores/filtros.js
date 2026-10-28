import { defineStore } from 'pinia'

export const useFiltrosStore = defineStore('filtros', {
  state: () => ({
    abierto: false
  }),
  actions: {
    abrir() {
      this.abierto = true
    },
    cerrar() {
      this.abierto = false
    },
    alternar() {
      this.abierto = !this.abierto
    }
  }
})
