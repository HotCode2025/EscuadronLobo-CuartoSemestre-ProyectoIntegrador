<template>
  <div class="toast-stack">
    <transition-group name="toast">
      <div
        v-for="m in toastStore.mensajes"
        :key="m.id"
        class="toast"
        :class="m.tipo"
        @click="toastStore.cerrar(m.id)"
      >
        {{ m.texto }}
      </div>
    </transition-group>
  </div>
</template>

<script setup>
import { useToastStore } from '../stores/toast'

const toastStore = useToastStore()
</script>

<style scoped>
.toast-stack {
  position: fixed;
  top: 20px;
  right: 20px;
  z-index: 1000;
  display: flex;
  flex-direction: column;
  gap: 10px;
  max-width: 320px;
}

.toast {
  padding: 14px 18px;
  border-radius: 10px;
  color: #161616;
  font-weight: 600;
  font-size: 0.95em;
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.3);
  cursor: pointer;
}

.toast.exito {
  background: var(--wf-success, #4CAF6D);
}
.toast.error {
  background: var(--wf-accent-2, #D63831);
  color: #fff;
}
.toast.info {
  background: var(--wf-accent, #F98103);
}

.toast-enter-active,
.toast-leave-active {
  transition: all 0.3s ease;
}
.toast-enter-from {
  opacity: 0;
  transform: translateX(30px);
}
.toast-leave-to {
  opacity: 0;
  transform: translateX(30px);
}

@media (max-width: 480px) {
  .toast-stack {
    left: 16px;
    right: 16px;
    max-width: none;
  }
}
</style>
