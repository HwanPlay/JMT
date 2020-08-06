import Vue from 'vue';
import Vuex from 'vuex';

import axios from 'axios';

// import router from '@/router';
import SERVER from '@/api/spring';
import router from '@/router';

Vue.use(Vuex);

export default new Vuex.Store({
  state: { // 중앙 관리할 데이터들의 집합
    accessToken: localStorage.getItem('accessToken'),
    refreshToken: null,
    userId: localStorage.getItem('userId'),
    myGroups: [],
    myProfile: [],

    emailValidationWord: '',
    isEmailOverlap: null,
    loginError: false,
  },
  getters: { // 데이터(state)를 가공해서 가져갈 함수들

    isLoggedIn(state) {
      if (state.accessToken === 'null') {
        return false;
      } else {
        return !!state.accessToken;
      }
    },
    config: state => ({ headers: { Authorization: `Token ${state.accessToken}` } })
  },

  mutations: { // 데이터를 변경하는 부분(commit을 통해 실행)
    SET_TOKEN(state, headers) {
      if (headers === null) {
        state.accessToken = null;
        state.refreshToken = null;
        state.userId = null;
        localStorage.setItem('accessToken', null);
        localStorage.setItem('refreshToken', null);
        localStorage.setItem('userId', null);
      }
      else {
        state.accessToken = headers.accesstoken;
        state.refreshToken = headers.refreshtoken;
        localStorage.setItem('accessToken', state.accessToken);
        localStorage.setItem('refreshToken', state.refreshToken);
      }
    },
    SET_VALIDATION_WORD(state, word) {
      if (word === 'fail') {
        state.isEmailOverlap = true;
        console.log('duplicated!');
      }
      else {
        state.emailValidationWord = word;
        state.isEmailOverlap = false;
        console.log('correct', state.emailValidationWord);
      }
    },
    SET_LOGIN_ERROR(state, val) {
      state.loginError = val;
    },
    SET_USER_ID(state, val) {
      localStorage.setItem('userId', val);
      state.userId = val;
    },
    SET_GROUP_INFO(state, val) {
      state.myGroups = val.data.groups;
      console.log(val.data.groups);
    }
  },
  actions: { // mutations에서 정의한 함수를 여기서 불러와서 실행함

    // 회원 정보와 관련된 액션들!!
    signup({ commit }, signupData) {
      const data = {
        'id': signupData.id,
        'name': signupData.name,
        'pw': signupData.pw
      };
      console.log('thisisdata', data, signupData);
      axios.post(SERVER.URL + SERVER.ROUTES.signup, data)
        .then(res => {
          console.log(res);
          commit('SET_LOGIN_ERROR', false);
        })
        .catch(err => console.log(err.response));
    },

    checkEmail({ commit }, email) {
      console.log(SERVER.URL + SERVER.ROUTES.checkEmail + '/' + email);
      axios.get(SERVER.URL + SERVER.ROUTES.checkEmail + '/' + email)
        .then(res => {
          console.log(res);
          commit('SET_VALIDATION_WORD', res.data);
        })
        .catch(err => console.log(err.response));
    },

    login({ commit }, loginData) {
      console.log(loginData);
      axios.post(SERVER.URL + SERVER.ROUTES.login, loginData)
        .then(res => {
          commit('SET_USER_ID', loginData.id);
          commit('SET_TOKEN', res.headers);
          commit('SET_LOGIN_ERROR', false);
        })
        .catch(err => {
          commit('SET_LOGIN_ERROR', true);
          console.log(err.response);
        });
    },

    logout({
      getters,
      commit
    }) {
      console.log(SERVER.URL + SERVER.ROUTES.logout + '/' + this.$store.state.userId, null, getters.config);
      axios.post(SERVER.URL + SERVER.ROUTES.logout, null, getters.config)
        .then(() => {
          commit('SET_TOKEN', null);
          commit('SET_LOGIN_ERROR', false);
          console.log('logout!');

        })
        .catch(err => console.log(err.response.data));
      console.log('logout!');
      commit('SET_TOKEN', null);
      commit('SET_LOGIN_ERROR', false);
    },

    // logout({ commit }){
    //   commit('SET_TOKEN', null);
    //   commit('SET_LOGIN_ERROR', false);    
    // },


    // 그룹과 관련된 기능들
    getGroupInfo({ state, commit }) {
      console.log('start!', state.userId);
      console.log('req:', SERVER.URL + SERVER.ROUTES.getGroupInfo + '/' + state.userId);
      axios.get(SERVER.URL + SERVER.ROUTES.getGroupInfo + '/' + state.userId)
        .then(res => {
          commit('SET_GROUP_INFO', res);
        });
    }
  },
  modules: {}
});
