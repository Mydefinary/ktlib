<script setup>
import Default from '@/layouts/default.vue'
</script>

<template>
  <VApp>
    <!-- Vuetify 기반 레이아웃 유지 -->
    <Default>
      <!-- router-view 를 children 으로 렌더링 -->
      <router-view />
    </Default>
  </VApp>
</template>

<style scoped>
/* 필요 시 스타일 작성 */
</style>
