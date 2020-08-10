import Vue from 'vue';
import Vuex, { Store } from 'vuex';

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
    myGroups: {},
    myName: localStorage.getItem('myName'),
    myPicture: localStorage.getItem('myPicture'),
    
    emailValidationWord: '',
    isEmailOverlap: null,
    loginError: false,
    groupModalOn: false,
  },
  getters: { // 데이터(state)를 가공해서 가져갈 함수들

    isLoggedIn(state){
      if (state.accessToken === 'null') {
        return false;
      }else{
        return !!state.accessToken;
      }
    },
  },

  mutations: { // 데이터를 변경하는 부분(commit을 통해 실행)
    SET_TOKEN(state, headers) {
      if (headers === null){
        localStorage.clear();
        state.accessToken = null;
        state.refreshToken = null;
        state.userId = null;
        router.push('Home');
      }
      else{
        state.accessToken = headers.accesstoken;
        state.refreshToken = headers.refreshtoken;
        localStorage.setItem('accessToken', state.accessToken);
        localStorage.setItem('refreshToken', state.refreshToken);
      }
    },

    REFRESH_ACCESS_TOKEN(state, token) {
      localStorage.setItem('accessToken', token);
      state.accessToken = token;
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
    },
    SET_LOGIN_ERROR(state, val){
      state.loginError = val;
    },
    SET_USER_ID(state, val){
      localStorage.setItem('userId', val);
      state.userId = val;
    },
    SET_GROUP_INFO(state, val){
      state.myGroups = val.data.groups;
    },
    SET_MY_PROFILE(state, val){
      console.log(val.data);
      localStorage.setItem('myName', val.data.name);
      localStorage.setItem('myPicture', val.data.profile_img);
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
      console.log(SERVER.URL + SERVER.ROUTES.checkEmail +'/'+email);
      axios.get(SERVER.URL + SERVER.ROUTES.checkEmail +'/'+email)
        .then(res => {
          commit('SET_VALIDATION_WORD', res.data);
        })
        .catch(err => console.log(err.response));
    },

    login({ state, commit }, loginData) {
      console.log(loginData);
      axios.post(SERVER.URL + SERVER.ROUTES.login, loginData)
        .then(res => {
          commit('SET_USER_ID', loginData.id);
          commit('SET_TOKEN', res.headers);
          commit('SET_LOGIN_ERROR', false);
          axios.get(SERVER.URL + SERVER.ROUTES.myProfile)
            .then((res) => {
              console.log('myprofile', res);
              commit('SET_MY_PROFILE', res);
            })
            .catch(err => console.log(err.response));
        })
        .catch(() => {
          commit('SET_LOGIN_ERROR', true);
        });
    },

    logout({ state, commit }) {
      console.log(SERVER.URL + SERVER.ROUTES.logout, state.userId);
      axios.post(SERVER.URL + SERVER.ROUTES.logout, {'id': state.userId})
        .then(() => {
        })
        .catch(err => console.log(err.response.data));  
      commit('SET_TOKEN', null);
      commit('SET_LOGIN_ERROR', false);
    },

    // 그룹과 관련된 기능들
    getGroupInfo({ state, commit }){
      console.log('req:', SERVER.URL + SERVER.ROUTES.getGroupInfo + '/' + state.userId);
      axios.get(SERVER.URL + SERVER.ROUTES.getGroupInfo + '/' + state.userId)
        .then(res => {
          commit('SET_GROUP_INFO', res);
        })
        .catch(err => console.log(err.response));
    },
  },
  modules: {}
});
