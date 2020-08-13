import axios from 'axios';

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
    // 응답 데이터를 가공
    return response;
  },
  function (error) {
    // 오류 응답을 처리
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
            console.log('thisisres',res);
            isRefreshing = false;
            console.log(isRefreshing);
            this.SET_TOKEN(res.data);
            return axios(originalRequest);
          }
        })
        .catch(err => console.log(err));
    }
    return Promise.reject(error);
  });


import { mapMutations } from 'vuex';

export default {
  methods:{
    ...mapMutations({
      SET_TOKEN: 'SET_TOKEN'
    })
  }
};