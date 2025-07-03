<template>
  <div>
    <h2>Dashboard</h2>
    <p v-if="message">{{ message }}</p>
    <button @click="logout">Logout</button>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import api from '../api/auth';
import { useRouter } from 'vue-router';

const router = useRouter();
const message = ref('');

const fetchData = async () => {
    try {
        const response = await api.get('/admin/dashboard'); // 예시: 관리자 권한 필요
        message.value = response.data;
    } catch (error) {
        if (error.response && error.response.status === 403) {
            alert('Access denied: You do not have permission.');
            router.push('/forbidden');
        } else {
            console.error(error);
        }
    }
};

const logout = () => {
    localStorage.removeItem('jwtToken');
    router.push('/login');
};

onMounted(fetchData);
</script>
