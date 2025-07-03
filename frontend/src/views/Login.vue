<template>
  <div class="d-flex flex-column align-center justify-center" style="height: 100vh;">
    <v-card width="500" class="pa-4">
      <v-card-title>Login</v-card-title>
      <v-card-text>
        <v-text-field v-model="form.email" label="Email" outlined full-width />
        <v-text-field v-model="form.password" label="Password" type="password" outlined full-width />
      </v-card-text>
      <v-card-actions class="d-flex flex-column">
        <v-btn @click="login" color="primary" block>로그인</v-btn>
        <v-btn @click="goToRegisterAuthor" color="secondary" block class="mt-2">작가 회원가입</v-btn>
        <v-btn @click="goToRegisterUser" color="secondary" block class="mt-2">독자 회원가입</v-btn>
      </v-card-actions>
    </v-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import api from '../api/auth'
import { useRouter } from 'vue-router'

const router = useRouter()
const form = ref({
    email: '',
    password: ''
})

const login = async () => {
    try {
        const response = await api.post('/api/user/login', form.value)
        localStorage.setItem('jwtToken', response.data)
        alert('Login successful!')
        router.push('/')
    } catch (error) {
        console.error(error.response?.data || error)
        alert('Login failed.')
    }
}

const goToRegisterAuthor = () => {
    router.push('/register-author')
}
const goToRegisterUser = () => {
    router.push('/register-user')
}
</script>
