<template>
  <div class="d-flex flex-column align-center justify-center" style="height: 100vh;">
    <v-card max-width="500" class="pa-4">
      <v-card-title>작가 회원가입</v-card-title>
      <v-card-text>
        <v-text-field v-model="form.authorName" label="이름" outlined />
        <v-text-field v-model="form.authorNickname" label="닉네임" outlined />
        <v-text-field v-model="form.email" label="이메일" outlined />
        <v-text-field v-model="form.authorPassword" label="비밀번호" type="password" outlined />
        <v-text-field v-model="form.phoneNumber" label="전화번호" outlined />
        <v-text-field v-model="form.portfolioUrl" label="포트폴리오 URL" outlined />
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
const form = ref({
  authorName: '',
  authorNickname: '',
  email: '',
  authorPassword: '',
  phoneNumber: '',
  portfolioUrl: '',
  status: 'Pending' // 초기 상태 기본값
})

const register = async () => {
  try {
    await api.post('/api/authors/register', form.value)
    alert('작가 회원가입 성공!')
    router.push('/login')
  } catch (error) {
    console.error('회원가입 실패 상세:', error.response?.data || error)
    alert('회원가입 실패: ' + (error.response?.data?.message || error.message))
}
  }
}
</script>