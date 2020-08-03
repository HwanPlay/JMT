import Vue from 'vue';
import Vuex from 'vuex';

import cookies from 'vue-cookies';
import axios from 'axios';

import router from '@/router';
import SERVER from '@/api/spring';

Vue.use(Vuex);

export default new Vuex.Store({
  state: { // 중앙 관리할 데이터들의 집합

    authToken: cookies.get('auth-token'),
    groups: [],
    myprofile: [],
  },
  getters: { // 데이터(state)를 가공해서 가져갈 함수들

    // isLoggedIn: state => !!state.authTokenm
    // config: state => ({ headers: { Authorization: `Token ${state.authToken}` } })
  },
  mutations: { // 데이터를 변경하는 부분(commit을 통해 실행)
    SET_TOKEN(state, token) {
      state.authToken = token;
      cookies.set('auth-token', token);
    },
  },
  actions: { // mutations에서 정의한 함수를 여기서 불러와서 실행함
    singup({
      dispatch
    }, signupData) {
      const info = {
        data: signupData,
        location: SERVER.ROUTES.signup
      };
      dispatch('postAuthData', info);
    },
    login({
      dispatch
    }, loginData) {
      const info = {
        data: loginData,
        location: SERVER.ROUTES.login
      };
      dispatch('postAuthData', info);
    },
    logout({
      getters,
      commit
    }) {
      axios.post(SERVER.URL + SERVER.ROUTES.logout, null, getters.config)
        .then(() => {
          commit('SET_TOKEN', null);
          cookies.remove('auth-token');
        })
        .catch(err => console.log(err.response.data));
    },

  },
  modules: {}
});
