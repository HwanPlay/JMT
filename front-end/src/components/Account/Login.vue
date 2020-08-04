<template>
  <v-main class="main">
    <v-container class="fill-height container" fluid>
      <v-row>

        <!-- 로그인 -->
        <v-col cols="12" sm="8" md="5" style= "margin-bottom: 100px; margin-left: 30px;">
          <v-spacer></v-spacer>
          <v-form>
            <h3 style="color: rgb(229, 235, 239);">모두를 위한 화상회의 플랫폼</h3>
            <h6 style=  "margin-top: 20px; color: rgb(187, 201, 224);">서비스를 시작하려면 로그인을 해주세요.</h6>
            <v-text-field v-model="loginData.id" dark filled background-color="rgb(52, 63, 87)" label="Email(ID)" name="login" style="margin-top: 30px;" append-icon="mdi-account" type="text"></v-text-field>
            <v-text-field v-model="loginData.pw" dark filled background-color="rgb(52, 63, 87)" id="Password" label="Password" name="password" append-icon="mdi-lock" type="password"></v-text-field>
          </v-form>
          <div class="float-left">
            <v-spacer></v-spacer>
              <v-btn class="mr-2" @click="login(loginData)" style="padding: 20px; background-color:rgb(244, 67, 72); color: white; outline:none;" >로그인</v-btn>
              <Signup>회원가입</Signup>
              <v-btn class="mr-2" style="padding: 20px; background-color:#1A237E; color: white; outline:none;" @click="developer">개발자</v-btn>
          </div>
          <div style="margin-top: 80px;">
            <div style="font-size: 13px; line-height: 18px; color:rgb(121, 134, 163);">
              계정 / 비밀번호가 기억이 안나신다면
              <span style="text-decoration: underline; cursor: pointer;">이곳</span>을 클릭해주세요
            </div>
          </div>
        </v-col>

        <!-- 캐러셀 -->
        <v-spacer></v-spacer>
        <v-col cols="12" md="6" style="padding: 12px 0px;">
          <v-spacer></v-spacer>
          <v-carousel :continuous="true" :cycle="cycle" :show-arrows="false" hide-delimiter-background delimiter-icon="mdi-minus" height="100%">
            <v-carousel-item v-for="(slide, i) in slides" :key="i">
              <v-row class="fill-height float-left" style="margin-left: 15px; " align="center" justify="center">
                <v-icon size=200>{{ icons[i] }}</v-icon>
                <h3 style="margin-bottom: 40px;">{{ slides[i] }} </h3>
              </v-row>
            </v-carousel-item>
          </v-carousel>
        </v-col>
      </v-row>
    </v-container>
  </v-main>
</template>

<script>
import Signup from './Signup';
import { mapActions } from 'vuex';

export default {
  name: 'Login',
  components: {
    Signup
  },

  data () {
    {
      return {
        icons: [
          'fas fa-users',
          'far fa-edit',
          'fas fa-solar-panel'
        ],
        cycle: true,
        slides: [
          '전 세계 사람들과, 언제 어디서든!',
          '강의를 보면서 필기도 가능합니다!',
          '사용자 친화적인 UI로 누구나 간편하게!'
        ],
        modalOpen: false,
        loginData: {
          id: null,
          pw: null,
        }
      };
    }
  },

  methods: {
    developer () {
      this.$emit('loginConfirm');
    },
    openModal () {
      console.log(this.modalOpen);
      this.modalOpen = !this.modalOpen;
    },
    closeModal () {
      this.modalOpen = !this.modalOpen;
    },
    ...mapActions(['login'])
    
  }
};
</script>

<style>
  .container{
    margin: auto;
    padding: 0 60px;
  }

</style>
