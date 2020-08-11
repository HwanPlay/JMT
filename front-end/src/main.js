import Vue from 'vue';
import App from './App.vue';
import router from './router';
import store from './store';
import vuetify from './plugins/vuetify';
import 'roboto-fontface/css/roboto/roboto-fontface.css';
import '@mdi/font/css/materialdesignicons.css';

import '../src/assets/sass/main.scss';

import { BootstrapVue, IconsPlugin ,BootstrapVueIcons } from 'bootstrap-vue';
import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap-vue/dist/bootstrap-vue.css';

// moment
import vueMoment from 'vue-moment';
// Install BootstrapVue
Vue.use(BootstrapVue);

Vue.use(BootstrapVueIcons);
// Optionally install the BootstrapVue icon components plugin
Vue.use(IconsPlugin);
Vue.use(vueMoment);

Vue.config.productionTip = false;

new Vue({
  router,
  store,
  vuetify,
  render: h => h(App),
  
}).$mount('#app');

import SERVER from '@/api/spring';
import axios from 'axios';
import { nodeName } from 'jquery';


axios.interceptors.request.use(
  function (config) {
    // 요청을 보내기 전에 수행할 일
    console.log('request interceptor');
    config.headers.Authorization = localStorage.getItem('accessToken');

    return config;
  },
  function (error) {
    // 오류 요청을 보내기전 수행할 일
    return Promise.reject(error);
  });

// 응답 인터셉터 추가
var isRefreshing = false;

axios.interceptors.response.use(
  function (response) {
    console.log('성공인 경우');
    // 응답 데이터를 가공
    return response;
  },
  function (error) {
    // 오류 응답을 처리
    console.log('에러인경우');
    
    const originalRequest = error.config;
    if (error.response.status === 401 && !isRefreshing){
      isRefreshing = true;
      const config = {
        headers:{
          refreshToken: localStorage.getItem('refreshToken'),
          accessToken: localStorage.getItem('accessToken')
        }
      };
      return axios.get(SERVER.URL + SERVER.ROUTES.reToken, config)
        .then(res => {
          if (res.status === 200){
            console.log('old', localStorage.getItem('accessToken'));
            store.commit('REFRESH_ACCESS_TOKEN', res.headers.accesstoken);
            isRefreshing = false;
            // localStorage.setItem('accessToken', res.headers.accesstoken);
            console.log('new Token!!!', localStorage.getItem('accessToken'));
            if (localStorage.getItem('accessToken') === undefined || !localStorage.getItem('accessToken')){
              return ;
            }
            return axios(originalRequest);
          }
        })
        .catch(err => console.log(err));
    }else if(error.response.status === 500 && isRefreshing){
      console.log('Expire refreshToken');
      localStorage.clear();
      store.commit('SET_TOKEN', null);
      isRefreshing = false;
      return ;
    }else{
      return Promise.reject(error);
    }
  });


