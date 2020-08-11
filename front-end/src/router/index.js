import Vue from 'vue';
import VueRouter from 'vue-router';

Vue.use(VueRouter);

const routes = [
  {
    path: '/Home',
    name: 'Home',
    component: () => import('../views/Home.vue')
  },
  {
    path: '/Group',
    name: 'Group',
    // component: () => import('../views/Group.vue')
    component: () => import('../views/Groupvuti.vue')
  },
  {
    path: '/Note',
    name: 'Note',
    component: () => import('../views/Note.vue')
  },
  {
    path: '/Conference/:roomId',
    name: 'Conference',
    props: true,
    component: () => import('../views/Conference.vue')
  },
  {
    path: '/Settings',
    name: 'Settings',
    component: () => import('../views/Settings.vue')
  }
];

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
});

export default router;
