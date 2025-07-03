<template>
  <div class="d-flex flex-column align-center justify-center" style="height: 100vh;">
    <v-card max-width="500" class="pa-4">
      <v-card-title>독자/관리자 회원가입</v-card-title>
      <v-card-text>
        <v-text-field v-model="form.name" label="이름" outlined />
        <v-text-field v-model="form.email" label="이메일" outlined />
        <v-text-field v-model="form.password" label="비밀번호" type="password" outlined />
        <v-text-field v-model="form.carrier" label="통신사" outlined />
        <v-select
          v-model="form.role"
          :items="roles"
          label="역할 선택"
          outlined
        />
      </v-card-text>
      <v-card-actions>
        <v-btn @click="register" color="primary" block>회원가입</v-btn>
      </v-card-actions>
    </v-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import api from '../../api/auth'
import { useRouter } from 'vue-router'

const router = useRouter()
const roles = ['USER', 'ADMIN']

const form = ref({
  name: '',
  email: '',
  password: '',
  carrier: '',
  role: 'USER', // 기본값 USER
  registeredAt: new Date(),
  point: 0
})

const register = async () => {
  try {
    await api.post('/api/users/register', form.value)
    alert('회원가입 성공!')
    router.push('/login')
  } catch (error) {
    console.error('회원가입 실패 상세:', error.response?.data || error)
    alert('회원가입 실패: ' + (error.response?.data?.message || error.message))
}
  }
}
</script>
