<template>
  <v-dialog Dark v-model="dialog" persistent max-width="500px">
    <template v-slot:activator="{ on, attrs }">
      <v-btn color="primary" v-bind="attrs" v-on="on" class="mr-2" style="width: 92.88px; padding: 20px; color: white; outline:none;">
        회원가입
      </v-btn>
    </template>
    <v-form ref="form" v-model="valid">
      <v-card class="mx-auto" max-width="500" style="background-color: rgb(187, 201, 224);">
        <v-card-title class="title font-weight-regular justify-space-between top">
          <span>{{ currentTitle }}</span>
          <v-avatar color="primary lighten-2" class="subheading white--text pt-2" size="24" v-text="step"></v-avatar>
        </v-card-title>

        <v-window v-model="step">
          <v-window-item :value="1">
            <v-card-text>
              <v-text-field v-model="signupData.name" :counter="10" :rules="Rules.name" label="Name" required></v-text-field>
              <v-row>
                <v-col cols="10" class="pr-0 pb-0">
                  <v-text-field v-model="signupData.id" :disabled="isEmailOverlap === false" :rules="Rules.email" label="E-mail(ID)" required></v-text-field>
                </v-col>
                <v-col cols="2" class="pl-0 pb-0">
                  <v-btn :loading='loading' :disabled="(isEmailOverlap === false) || !mail || loading" class="ma-2" outlined color="black" @click="checkEmail()" style="outline: none;">인증</v-btn>
                </v-col>
              </v-row>
              <v-alert :value="isEmailOverlap" color="pink" dark border="left" icon="fa-exclamation" transition="scale-transition"> 
                <div>이미 사용중인 이메일입니다!</div>
              </v-alert>
              <v-row v-show="isEmailOverlap === false">
                <v-col cols="8" class="pr-0 pt-0">
                  <v-text-field v-model="verificationWord" :disabled="isVerified" :rules="Rules.verificationWord" label="인증 문자 확인" required></v-text-field>
                </v-col>
                <v-col cols="2" class="pl-0 pt-0">
                  <v-btn :disabled="isVerified" class="ma-2" outlined style="outline: none;" color="black" @click="emailVerify">
                    <span v-if="!isVerified">확인</span>
                    <span v-if="isVerified">인증완료</span>
                    </v-btn>
                </v-col>
              </v-row>
              <v-alert :value="isVerified === false" color="pink" dark border="left" icon="fa-exclamation" transition="scale-transition"> 
                <div>인증 코드를 다시 확인해주세요!</div>
              </v-alert>
              <span class="caption grey--text text--darken-1">서비스 사용에 필요한 이름과 이메일을 입력해주세요</span>
            </v-card-text>
          </v-window-item>

          <v-window-item :value="2">
            <v-card-text>
              <v-text-field v-model="signupData.pw" label="Password" :rules="Rules.password" type="password" required></v-text-field>
              <v-text-field v-model="passwordConfirm" label="Confirm Password" :rules="passwordConfirmRules" type="password" required></v-text-field>
              <span class="caption grey--text text--darken-1">
                로그인 시 사용할 비밀번호를 입력해주세요
              </span>
            </v-card-text>
          </v-window-item>
          <v-window-item :value="3">
            <div class="pa-4 text-center">
              <v-img contain height="200" :src="require('../../JMTwithLogo.png')"></v-img>
              <h3 class="title mb-2">JMT는 당신을 환영합니다!</h3>
              <span class="caption" style="color: rgb(52, 63, 87);">가입해주셔서 감사합니다</span>
              <div>
                <v-btn class="font-weight-bold" style="outline: none; margin-top: 20px;" text @click="signup(signupData); close();">닫기</v-btn>
              </div>
            </div>
          </v-window-item>
        </v-window>

      <v-divider></v-divider>

      <v-card-actions>
        <v-btn :disabled="step === 3" @click="alert = !alert" text style="outline: none;">
          <div v-if="!alert">Close</div>
          <div v-if="alert">Continue</div>
          </v-btn>
        <!-- <v-btn :disabled="step === 1" text @click="step--" style="outline:none;">Back</v-btn> -->
        <v-spacer></v-spacer>
        <v-btn :disabled="(!valid) || (step === 3) || (!isVerified)" text depressed @click="step++" style="outline: none;"> Next 
        </v-btn>
      </v-card-actions>
      <v-alert :value="alert" dense type='error' prominent color="pink" dark border="left" class="m-0" transition="scale-transition">
        지금 취소하면 작성한 모든 정보가 사라집니다. <br> 회원가입 취소를 원하시면 
        <strong><span style='text-decoration: underline; cursor: pointer;' @click='close'>
          이곳
        </span></strong>을 클릭해주세요
      </v-alert>
    </v-card>
    </v-form>
  </v-dialog>
</template>

<script>
import SERVER from '../../api/spring.js';
import axios from 'axios';

import { mapActions } from 'vuex';

export default {
  name: 'Signup',
  data: () => ({
    dialog: false,
    valid: true,
    step: 1,
    mail: false,
    alert: false,
    signupData:{
      name: '',
      id: '',
      pw: '',
    },
    loading: false,
    verificationWord: '',
    validationWord: '',
    isEmailOverlap: null,
    isVerified: null,
    passwordConfirm: '',
    Rules: {
      name: [
        v => !!v || '이름을 입력해주세요',
        v => (v && v.length <= 10) || '10자 이내의 이름을 입력해주세요'
      ],
      email: [
        v => !!v || 'E-mail을 입력해주세요',
        v => /.+@.+\..+/.test(v) || '바른 E-mail 형식을 입력해주세요',
      ],
      verificationWord: [
        v => !!v || '인증번호를 입력해주세요'
      ],
      password: [
        v => !!v || '패스워드를 입력해주세요',
        v => (v && v.length >= 8) || '8자 이상의 비밀번호를 입력해주세요',
      ],    
    }
  }),
  methods: {
    close () {
      this.alert = false;  // 닫기 창 제거
      this.step = 1;  // 모달 처음으로 되돌리기
      this.resetValidation();  // 유효성검사 제거
      this.dialog = false;  // 모달 제거
      this.isVerified = null;
      this.isEmailOverlap = null;
    },
    resetValidation() {
      this.$refs.form.reset();  
    },
    validate(){
      this.$refs.form.validate();
    },
    ...mapActions(['signup']),
    emailVerify(){
      if (this.verificationWord == this.validationWord){
        this.isVerified = true;
      }else{
        this.isVerified = false;
      }
    },
    emailValid(){
      if (/.+@.+\..+/.test(this.signupData.id))
      {
        this.mail = true;
      } else{
        this.mail = false;
      }
    },
    checkEmail(){
      this.isEmailOverlap = null;
      this.loading = true;
      axios.get(SERVER.URL + SERVER.ROUTES.checkEmail + '/' + this.signupData.id)
        .then(res => {
          if (res.data === 'fail'){
            this.isEmailOverlap = true;
          }
          else{
            this.validationWord = res.data;
            console.log(res.data);
            this.isEmailOverlap = false;
          }
          this.loading = false;
        })
        .catch(err => console.log(err));
    }
  },
  computed: {
    passwordConfirmRules () {
      return (this.signupData.pw === this.passwordConfirm) || Array('패스워드가 일치하지 않습니다.');
    },
    currentTitle(){
      switch(this.step){
      case 1: return 'JMT 회원 가입';
      case 2: return '비밀번호 생성';
      default: return '계정 생성 완료';
      }
    },
  },
  watch: {
    signupData:{
      deep: true,
      handler(){
        this.emailValid();
      }
    },
  }
};
</script>

<style>
  .custom-loader {
    animation: loader 1s infinite;
    display: flex;
  }
  @-moz-keyframes loader {
    from {
      transform: rotate(0);
    }
    to {
      transform: rotate(360deg);
    }
  }
  @-webkit-keyframes loader {
    from {
      transform: rotate(0);
    }
    to {
      transform: rotate(360deg);
    }
  }
  @-o-keyframes loader {
    from {
      transform: rotate(0);
    }
    to {
      transform: rotate(360deg);
    }
  }
  @keyframes loader {
    from {
      transform: rotate(0);
    }
    to {
      transform: rotate(360deg);
    }
  }
  .top {
    background-color: rgb(52, 63, 87);
    color: white;
  }
</style>
