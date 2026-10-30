<template>
  <div class="login-page">
    <div class="login-card">
      <h1><Icono name="llave" /> Iniciar Sesión</h1>

      <form @submit.prevent="handleLogin">
        <input
          v-model="email"
          type="email"
          placeholder="Correo electrónico"
          required
        />

        <input
          v-model="password"
          type="password"
          placeholder="Contraseña"
          required
        />

        <button type="submit">Entrar</button>
      </form>

      <p v-if="error" class="error">{{ error }}</p>

      <p class="registro">
        ¿No tenés cuenta?
        <router-link to="/register">Registrate acá</router-link>
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import Icono from '../components/Icono.vue'

const router = useRouter()
const authStore = useAuthStore()
const email = ref('')
const password = ref('')
const error = ref('')

async function handleLogin() {
  try {
    const user = await authStore.login(email.value, password.value)
    if (user.rol === 'ADMINISTRADOR') router.push('/admin')
    else router.push('/')
  } catch (err) {
    error.value = 'Error al iniciar sesión. Verificá tus credenciales.'
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--wf-bg);
  font-family: var(--wf-font-body);
  color: var(--wf-text);
}

.login-card {
  background-color: var(--wf-surface);
  border: 1px solid var(--wf-border);
  border-radius: 15px;
  padding: 40px;
  width: 100%;
  max-width: 400px;
  text-align: center;
}

.login-card h1 {
  font-size: 1.8em;
  margin-bottom: 20px;
  color: var(--wf-text);
}

form {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

input {
  padding: 12px;
  border: 1px solid var(--wf-border);
  border-radius: 8px;
  font-size: 1em;
  outline: none;
  transition: 0.3s;
  background-color: var(--wf-bg-deep);
  color: var(--wf-text);
}

input:focus {
  border-color: var(--wf-accent);
  box-shadow: 0 0 6px rgba(249, 129, 3, 0.3);
}

button[type='submit'] {
  padding: 12px;
  background: var(--wf-accent);
  color: #161616;
  border: none;
  border-radius: 10px;
  font-size: 1.1em;
  cursor: pointer;
  transition: 0.3s;
  font-weight: 700;
}

button[type='submit']:hover {
  background: var(--wf-accent-hover);
  transform: scale(1.02);
}

.error {
  color: var(--wf-accent-2);
  margin-top: 15px;
  font-weight: bold;
}

.registro {
  margin-top: 25px;
  color: var(--wf-text-muted);
}

.registro a {
  color: var(--wf-accent);
  text-decoration: none;
  font-weight: bold;
}

.registro a:hover {
  text-decoration: underline;
}

@media (max-width: 480px) {
  .login-card {
    padding: 24px;
  }
}
</style>
