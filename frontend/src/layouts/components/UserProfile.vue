<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const avatarBadgeProps = {
  dot: true,
  location: 'bottom right',
  offsetX: 3,
  offsetY: 3,
  color: 'success',
  bordered: true,
}

// ✅ JWT 기반 유저명, 역할 표시
const userName = ref('Guest')
const userRole = ref('Role')

onMounted(() => {
  const token = localStorage.getItem('jwtToken')
  if (token) {
    try {
      const payload = JSON.parse(atob(token.split('.')[1]))
      userName.value = payload.sub || payload.email || 'Unknown User'
      userRole.value = payload.role || 'Unknown Role'
    } catch (e) {
      console.error('Invalid token', e)
    }
  }
})

// ✅ 로그아웃 함수
const logout = () => {
  localStorage.removeItem('jwtToken')
  router.push('/login')
}
</script>

<template>
  <VBadge v-bind="avatarBadgeProps">
    <VAvatar style="cursor: pointer;" color="primary" variant="tonal">
      <VIcon icon="mdi-account" />
      <!-- Menu -->
      <VMenu activator="parent" width="230" location="bottom end" offset="14px">
        <VList>
          <!-- User Avatar & Name -->
          <VListItem>
            <template #prepend>
              <VListItemAction start>
                <VBadge v-bind="avatarBadgeProps">
                  <VAvatar color="primary" size="40" variant="tonal">
                    <VIcon icon="mdi-account" />
                  </VAvatar>
                </VBadge>
              </VListItemAction>
            </template>
            <VListItemTitle class="font-weight-semibold">{{ userName }}</VListItemTitle>
            <VListItemSubtitle>{{ userRole }}</VListItemSubtitle>
          </VListItem>

          <VDivider class="my-2" />

          <!-- Logout -->
          <VListItem @click="logout">
            <template #prepend>
              <VIcon class="me-2" icon="mdi-logout-variant" size="22" />
            </template>
            <VListItemTitle>Logout</VListItemTitle>
          </VListItem>
        </VList>
      </VMenu>
    </VAvatar>
  </VBadge>
</template>
