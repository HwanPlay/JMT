<template>
  <v-main style="background-color: rgb(7, 14, 29);">
    <v-container class="fill-height">
      <v-row>

        <!-- 로그인 -->
        <v-col cols="5">
          <v-spacer></v-spacer>
          <v-form>
            <h3 style="color: rgb(229, 235, 239);">모두를 위한 화상회의 플랫폼</h3>
            <h6 style=  "margin-top: 20px; color: rgb(187, 201, 224);">서비스를 시작하려면 로그인을 해주세요.</h6>
            <v-text-field v-model="loginData.id" dark filled background-color="rgb(52, 63, 87)" label="Email(ID)" name="login" style="margin-top: 30px;" append-icon="mdi-account" type="text" @keypress.enter="login(loginData)"></v-text-field>
            <v-text-field v-model="loginData.pw" dark filled background-color="rgb(52, 63, 87)" id="Password" label="Password" name="password" append-icon="mdi-lock" type="password" @keypress.enter="login(loginData)"></v-text-field>
          </v-form>
          <div v-if="this.$store.state.loginError" style="color:#EF5350; margin-bottom: 20px;">ID / 비밀번호를 확인해주세요!</div>
          <div class="float-left">
            <v-spacer></v-spacer>
              <v-btn class="mr-2" @click="login(loginData)" style="padding: 20px; background-color:rgb(244, 67, 72); color: white; outline:none;" >로그인</v-btn>
              <Signup>회원가입</Signup>
          </div>
          <div style="margin-top: 80px;">
            <div style="font-size: 13px; line-height: 18px; color:rgb(121, 134, 163);">
              비밀번호가 기억이 안나신다면
              <span style="text-decoration: underline; cursor: pointer;" @click="passwordModalOn=true">이곳</span>을 클릭해주세요
            </div>
            <v-dialog width='500px' v-model="passwordModalOn">
              <FindPassword @close='passwordModalOn=false' />
            </v-dialog>
          </div>
        </v-col>

        <!-- 캐러셀 -->
        <v-spacer></v-spacer>
        <!-- <v-col cols="12" md="6" style="padding: 12px 0px;"> -->
        <v-col cols="6">
          <v-carousel :continuous="true" :cycle="cycle" :show-arrows="false" hide-delimiter-background delimiter-icon="mdi-minus" height="100%">
            <v-carousel-item v-for="(slide, i) in slides" :key="i">
              <v-row class="fill-height" style="margin: 0 15px 0 15px;" align="center" justify="center">
                <v-icon size=200>{{ icons[i] }}</v-icon>
                <h3 style="margin-bottom: 40px; text-align: center;">{{ slides[i] }} </h3>
              </v-row>
            </v-carousel-item>
          </v-carousel>
        </v-col>

      </v-row>
    </v-container>
  </v-main>
</template>

<script>
import { mapActions } from 'vuex';

import Signup from './Signup';
import FindPassword from './FindPassword.vue';

export default {
  name: 'Login',
  components: {
    Signup,
    FindPassword
  },

  data () {
  
    return {
      icons: [
        'fas fa-users',
        'far fa-edit',
        'fas fa-chalkboard-teacher'
      ],
      cycle: true,
      slides: [
        '전 세계 사람들과, 언제 어디서든!',
        '강의를 보면서 필기도 가능합니다!',
        '직관적인 UI로 누구나 간편하게!'
      ],
      modalOpen: false,
      loginData: {
        id: null,
        pw: null,
      },
      passwordModalOn: false
    };
  
  },

  methods: {
    openModal () {
      this.modalOpen = !this.modalOpen;
    },
    closeModal () {
      this.modalOpen = !this.modalOpen;
    },
    ...mapActions(['login'])
    
  }
};
</script>