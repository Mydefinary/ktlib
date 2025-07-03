import { createRouter, createWebHashHistory } from 'vue-router';

// 토큰 파싱하여 role을 가져오기 위한 유틸
function getRoleFromToken() {
  const token = localStorage.getItem('jwtToken');
  if (!token) return null;
  const payload = JSON.parse(atob(token.split('.')[1]));
  return payload.role;
}

const routes = [
  {
    path: '/',
    component: () => import('../components/pages/Index.vue'),
    meta: { requiresAuth: true },
  },
  {
    path: '/authors',
    component: () => import('../components/ui/AuthorGrid.vue'),
    meta: { requiresAuth: true, roles: ['AUTHOR'] },
  },
  {
    path: '/authorLists',
    component: () => import('../components/AuthorListView.vue'),
    meta: { requiresAuth: true, roles: ['AUTHOR'] },
  },
  {
    path: '/manuscripts',
    component: () => import('../components/ui/ManuscriptGrid.vue'),
    meta: { requiresAuth: true, roles: ['AUTHOR', 'ADMIN'] },
  },
  {
    path: '/manuscriptLists',
    component: () => import('../components/ManuscriptListView.vue'),
    meta: { requiresAuth: true, roles: ['AUTHOR', 'ADMIN'] },
  },
  {
    path: '/publishes',
    component: () => import('../components/ui/PublishGrid.vue'),
    meta: { requiresAuth: true, roles: ['AUTHOR', 'ADMIN'] },
  },
  {
    path: '/books',
    component: () => import('../components/ui/BookGrid.vue'),
    meta: { requiresAuth: true, roles: ['USER', 'AUTHOR', 'ADMIN'] },
  },
  {
    path: '/bookLists',
    component: () => import('../components/BookListView.vue'),
    meta: { requiresAuth: true, roles: ['USER', 'AUTHOR', 'ADMIN'] },
  },
  {
    path: '/points',
    component: () => import('../components/ui/PointGrid.vue'),
    meta: { requiresAuth: true, roles: ['USER', 'ADMIN'] },
  },
  {
    path: '/pointsLists',
    component: () => import('../components/PointsListView.vue'),
    meta: { requiresAuth: true, roles: ['USER', 'ADMIN'] },
  },
  {
    path: '/users',
    component: () => import('../components/ui/UserGrid.vue'),
    meta: { requiresAuth: true, roles: ['USER'] },
  },
  {
    path: '/subscribeSus',
    component: () => import('../components/ui/SubscribeSuGrid.vue'),
    meta: { requiresAuth: true, roles: ['USER', 'ADMIN'] },
  },
  {
    path: '/subscribeLists',
    component: () => import('../components/SubscribeListView.vue'),
    meta: { requiresAuth: true, roles: ['USER', 'ADMIN'] },
  },
  {
    path: '/periodSubscribes',
    component: () => import('../components/ui/PeriodSubscribeGrid.vue'),
    meta: { requiresAuth: true, roles: ['USER', 'ADMIN'] },
  },
  // JWT 기반 추가 페이지
  {
    path: '/login',
    component: () => import('../views/Login.vue'),
  },
  {
    path: '/register',
    component: () => import('../views/Register.vue'),
  },
  {
    path: '/forbidden',
    component: () => import('../views/Forbidden.vue'),
  },
  {
    path: '/register-author',
    component: () => import('../components/pages/RegisterAuthor.vue'),
  },
  {
      path: '/register-user',
      component: () => import('../components/pages/RegisterUser.vue'),
  },

];

const router = createRouter({
  history: createWebHashHistory(),
  routes,
});

// ✅ 인증 + 권한 기반 라우팅 보호
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('jwtToken');
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth);
  const allowedRoles = to.meta.roles;

  if (requiresAuth && !token) {
    // 인증 필요 경로인데 토큰 없으면 로그인으로 이동
    return next('/login');
  }

  if (requiresAuth && allowedRoles) {
    const userRole = getRoleFromToken();
    if (!allowedRoles.includes(userRole)) {
      // 권한 없으면 forbidden 페이지로 이동
      return next('/forbidden');
    }
  }

  next();
});

export default router;
