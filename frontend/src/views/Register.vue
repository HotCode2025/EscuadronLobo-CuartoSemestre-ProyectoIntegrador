<template>
  <div class="register-card">
    <h1>Crear Cuenta</h1>

    <form @submit.prevent="handleRegister">
      <h3>Datos personales</h3>
      <input v-model="form.nombre" type="text" placeholder="Nombre completo" required class="input-estilo" />
      <input v-model="form.email" type="email" placeholder="Correo electrónico" required class="input-estilo" />
      <input v-model="form.contraseña" type="password" placeholder="Contraseña" required class="input-estilo" />
      <input v-model="form.numeroTelefonico" type="tel" placeholder="Número de teléfono" required class="input-estilo" />

      <h3>Dirección</h3>
      <input v-model="form.direccion.calle" type="text" placeholder="Calle" required class="input-estilo" />
      <input v-model="form.direccion.ciudad" type="text" placeholder="Ciudad" required class="input-estilo" />
      <input v-model="form.direccion.estado" type="text" placeholder="Estado / Provincia" required class="input-estilo" />
      <input v-model="form.direccion.codigoPostal" type="text" placeholder="Código Postal" required class="input-estilo" />
      <input v-model="form.direccion.pais" type="text" placeholder="País" required class="input-estilo" />

      <button type="submit">Registrarse</button>
    </form>

    <p v-if="error" class="mensaje error">{{ error }}</p>
    <p v-if="success" class="mensaje exito">{{ success }}</p>

    <p class="login-link">
      ¿Ya tenés cuenta?
      <router-link to="/login">Iniciá sesión</router-link>
    </p>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import api from '../services/api'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const authStore = useAuthStore()
const error = ref('')
const success = ref('')

const form = ref({
  nombre: '',
  email: '',
  contraseña: '',
  numeroTelefonico: '',
  rol: 'USUARIO',
  direccion: {
    calle: '',
    ciudad: '',
    estado: '',
    codigoPostal: '',
    pais: ''
  }
})

async function handleRegister() {
  try {
    error.value = ''
    success.value = ''

    const { data } = await api.post('/auth/register', form.value)

    authStore.token = data.token
    authStore.user = data.usuario
    localStorage.setItem('token', data.token)
    localStorage.setItem('user', JSON.stringify(data.usuario))

    success.value = 'Registro exitoso. Redirigiendo...'
    setTimeout(() => router.push('/'), 1200)
  } catch (err) {
    error.value = 'Error al registrarse: ' + (err.response?.data?.message || err.message)
  }
}
</script>

<style scoped>
.register-card {
  max-width: 600px;
  margin: 50px auto;
  padding: 40px;
  background-color: var(--wf-surface);
  border: 1px solid var(--wf-border);
  border-radius: 15px;
  font-family: var(--wf-font-body);
  color: var(--wf-text);
}

.register-card h1 {
  text-align: center;
  color: var(--wf-text);
  font-size: 1.9em;
  margin-bottom: 25px;
}

.register-card h3 {
  color: var(--wf-text-muted);
  margin: 20px 0 5px;
  font-size: 1em;
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

form {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.input-estilo {
  padding: 12px 15px;
  border-radius: 10px;
  border: 1px solid var(--wf-border);
  background-color: var(--wf-bg-deep);
  font-size: 1em;
  color: var(--wf-text);
  transition: border 0.3s, box-shadow 0.3s;
}
.input-estilo:focus {
  border-color: var(--wf-accent);
  box-shadow: 0 0 5px rgba(249, 129, 3, 0.4);
  outline: none;
}

button[type='submit'] {
  margin-top: 10px;
  padding: 14px;
  background-color: var(--wf-accent);
  color: #161616;
  border: none;
  border-radius: 10px;
  font-size: 1.1em;
  font-weight: 700;
  cursor: pointer;
  transition: 0.3s;
}
button[type='submit']:hover {
  background-color: var(--wf-accent-hover);
  transform: scale(1.02);
}

.mensaje {
  margin-top: 15px;
  text-align: center;
}
.mensaje.error {
  color: var(--wf-accent-2);
}
.mensaje.exito {
  color: var(--wf-success);
}

.login-link {
  text-align: center;
  margin-top: 25px;
  color: var(--wf-text-muted);
}
.login-link a {
  color: var(--wf-accent);
  font-weight: bold;
  text-decoration: none;
}
.login-link a:hover {
  text-decoration: underline;
}

@media (max-width: 480px) {
  .register-card {
    margin: 20px auto;
    padding: 24px;
  }
}
</style>
