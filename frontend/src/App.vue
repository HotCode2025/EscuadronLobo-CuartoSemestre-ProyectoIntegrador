<template>
  <div>
    <Navbar />
    <router-view />
    <Footer />
    <ToastContainer />
  </div>
</template>

<script setup>
import Navbar from './components/Navbar.vue'
import Footer from './components/Footer.vue'
import ToastContainer from './components/ToastContainer.vue'
</script>