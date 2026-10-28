import { defineStore } from 'pinia'

let nextId = 1

export const useToastStore = defineStore('toast', {
  state: () => ({
    mensajes: []
  }),
  actions: {
    mostrar(texto, tipo = 'info', duracion = 3500) {
      const id = nextId++
      this.mensajes.push({ id, texto, tipo })
      setTimeout(() => this.cerrar(id), duracion)
    },
    exito(texto) {
      this.mostrar(texto, 'exito')
    },
    error(texto) {
      this.mostrar(texto, 'error')
    },
    info(texto) {
      this.mostrar(texto, 'info')
    },
    cerrar(id) {
      this.mensajes = this.mensajes.filter(m => m.id !== id)
    }
  }
})
