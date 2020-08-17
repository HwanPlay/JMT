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
    component: () => import('../views/Group.vue')
  },
  {
    path: '/Note',
    name: 'Note',
    component: () => import('../views/Editor.vue')
  },
  {
    path: '/Conference/:roomId',
    name: 'Conference',
    props: true,
    component: () => import('../views/Conference.vue')
  },
  {
    path: '/devConference',
    name: 'devConference',
    props: true,
    component: () => import('../components/Conference/dev/devConference.vue')
  },
  {
    path: '/Settings',
    name: 'Settings',
    component: () => import('../views/Settings.vue')
  },
  {
    path: '/Profile',
    name: 'Profile',
    component: () => import('../views/Profile.vue')
  },
  {
    path: '/editor',
    name: 'Editor',
    component: () => import('../views/Editor.vue')
  },
  {
    path: '/editor/detail',
    name: 'EditorDetail',
    component: () => import('../views/EditorDetail.vue')
  }
];

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
});

export default router;
