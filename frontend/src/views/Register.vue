<template>
  <div>
    <h2>Register</h2>
    <form @submit.prevent="register">
      <input v-model="form.name" placeholder="Name" required />
      <input v-model="form.email" placeholder="Email" required />
      <input v-model="form.password" type="password" placeholder="Password" required />
      <button type="submit">Register</button>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import api from '../api/auth';
import { useRouter } from 'vue-router';

const router = useRouter();
const form = ref({
    name: '',
    email: '',
    password: ''
});

const register = async () => {
    try {
        await api.post('/api/user/register', form.value);
        alert('Registration successful! Please login.');
        router.push('/login');
    } catch (error) {
        console.error(error);
        alert('Registration failed.');
    }
};
</script>
