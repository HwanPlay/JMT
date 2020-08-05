import Vue from 'vue';
import Vuex from 'vuex';

import cookies from 'vue-cookies';
import axios from 'axios';

// import router from '@/router';
import SERVER from '@/api/spring';
import router from '@/router';

Vue.use(Vuex);

export default new Vuex.Store({
  state: { // 중앙 관리할 데이터들의 집합
    accessToken: null,
    refreshToken: null,
    groups: [],
    myprofile: [],
    emailValidationWord: '',
    isEmailOverlap: null,
  },
  getters: { // 데이터(state)를 가공해서 가져갈 함수들

    isLoggedIn: state => !!state.accessToken,
    config: state => ({ headers: { Authorization: `Token ${state.authToken}` } })
  },
  mutations: { // 데이터를 변경하는 부분(commit을 통해 실행)
    SET_TOKEN(state, headers) {
      if (headers === null){
        state.accessToken = null;
        state.refreshToken = null;
        localStorage.setItem('accessToken', null);
        localStorage.setItem('refreshToken', null);        
      }
      else{
        state.accessToken = headers.accesstoken;
        state.refreshToken = headers.refreshtoken;
        localStorage.setItem('accessToken', state.accessToken);
        localStorage.setItem('refreshToken', state.refreshToken);
      }
    },
    SET_VALIDATION_WORD(state, word){
      if (word === 'fail'){
        state.isEmailOverlap = true;
        console.log('duplicated!');
      }
      else{
        state.emailValidationWord = word;
        state.isEmailOverlap = false;
        console.log('correct', state.emailValidationWord);
      }
    }
  },
  actions: { // mutations에서 정의한 함수를 여기서 불러와서 실행함
    postAuthData({ commit }, info) {
      console.log('hi');
      axios.post(SERVER.URL + info.location, info.data)
        .then(res => {
          commit('SET_TOKEN', res.headers);
        })
        .catch(err => console.log(err.response));
    },

    signup({ state }, signupData) {
      const data = {
        'id': signupData.id,
        'name': signupData.name,
        'pw': signupData.pw
      };
      console.log('thisisdata', data);
      axios.post(SERVER.URL + SERVER.ROUTES.signup, data)
        .then(res => {
          console.log(res);
          router.push({ name: 'Home'});
        })
        .catch(err => console.log(err.response));
    },

    checkEmail({ commit }, email) {
      console.log(SERVER.URL + SERVER.ROUTES.checkEmail +'/'+email);
      axios.get(SERVER.URL + SERVER.ROUTES.checkEmail +'/'+email)
        .then(res => {
          console.log(res);
          commit('SET_VALIDATION_WORD', res.data);
        });
    },

    login({
      dispatch
    }, loginData) {
      const info = {
        data: loginData,
        location: SERVER.ROUTES.login,
      };
      dispatch('postAuthData', info);
    },

    logout({
      // getters,
      commit
    }) {
      // axios.post(SERVER.URL + SERVER.ROUTES.logout, null, getters.config)
      //   .then(() => {
      //     commit('SET_TOKEN', null);
      //     cookies.remove('auth-token');
      //   })
      //   .catch(err => console.log(err.response.data));
      console.log('logout!');
      commit('SET_TOKEN', null);
    },

  },
  modules: {}
});
