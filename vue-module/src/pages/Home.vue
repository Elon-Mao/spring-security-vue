<script setup lang="ts">
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
const router = useRouter()
const userStore = useUserStore()
if (userStore.user === null) {
  router.push('/login')
}

const logout = async () => {
  userStore.deleteUser()
  router.push('/login')
}

</script>

<template>
  <h1>{{ 'Hello, ' + userStore.user?.username }}</h1>
  <button @click="logout">logout</button>
</template>

<style scoped></style>
