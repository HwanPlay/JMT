<template>
    <v-form ref="form" v-model="valid">
      <v-card class="mx-auto" max-width="500" style="background-color: rgb(187, 201, 224);">
        <v-card-title class="title top font-weight-regular justify-space-between">
          <span>{{ currentTitle }}</span>
        </v-card-title>

        <v-window v-model="step">
          <v-window-item :value="1">
            <v-card-text>
              <v-row>
                <v-col cols="10" class="pr-0 pb-0">
                  <v-text-field v-model="email" :rules="Rules.email" label="E-mail(ID)" required></v-text-field>
                </v-col>
                <v-col cols="2" class="pl-0 pb-0">
                  <v-btn :loading='loading' :disabled="(mailCheck === false) || !mail || loading" class="ma-2" outlined color="black" @click="mailCheck = null; checkEmail(); loader='loading'" style="outline: none;">인증</v-btn>
                </v-col>
              </v-row>
              <v-alert :value="mailCheck" color="error" dark border="top" icon="fa-exclamation" transition="scale-transition"> 
                <div>존재하지 않는 회원입니다!</div>
              </v-alert>
              <v-row v-show="mailCheck === false">
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
              <v-alert :value="isVerified === false" color="pink" dark border="top" icon="fa-exclamation" transition="scale-transition"> 
                <div>인증 코드를 다시 확인해주세요!</div>
              </v-alert>
              <span class="caption grey--text text--darken-1">사용하시던 이메일을 입력해주세요</span>
            </v-card-text>
          </v-window-item>

          <v-window-item :value="2">
            <v-card-text>
              <v-text-field v-model="pw" label="Password" :rules="Rules.password" type="password" required></v-text-field>
              <v-text-field v-model="passwordConfirm" label="Confirm Password" :rules="passwordConfirmRules" type="password" required></v-text-field>
              <span class="caption grey--text text--darken-1">
                새로운 패스워드를 설정해주세요
              </span>
            </v-card-text>
          </v-window-item>
          <v-window-item :value="3">

            <div class="pa-4 text-center">
              <v-img contain height="200" :src="require('../../JMTwithLogo.png')"></v-img>
              <h3 class="title mb-2">패스워드 재설정이 완료되었습니다</h3>
              <span class="caption" style="color: rgb(52, 63, 87);">이용해주셔서 감사합니다!</span>
              <div>
                <v-btn class="font-weight-bold" style="outline: none; margin-top: 20px;" text @click="close(); changePw();">닫기</v-btn>
              </div>
            </div>
          </v-window-item>
        </v-window>

      <v-divider></v-divider>

      <v-card-actions>
        <v-btn :disabled="step === 3" @click="close();" text style="outline: none;">
          Close
          </v-btn>
        <!-- <v-btn :disabled="step === 1" text @click="step--" style="outline:none;">Back</v-btn> -->
        <v-spacer></v-spacer>
        <v-btn :disabled="(!valid) || (step === 3) || (!isVerified)" text depressed @click="stepPlus()" style="outline: none;"> Next 
        </v-btn>
      </v-card-actions>
    </v-card>
    </v-form>
</template>

<script>
import axios from 'axios';
import SERVER from '../../api/spring.js';

export default {
  name: 'findPassword',
  data(){
    return {
      valid: true,
      step: 1,
      mail: false,
      alert: false,
      email: '',
      pw: '',
      loader: null,
      loading: false,
      verificationWord: '',
      AuthorWord: '',
      isVerified: null,
      passwordConfirm: '',
      mailCheck: null,
      Rules: {
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
      },
      inputData: {
        id: '',
        pw: ''
      },
    };
  },
  methods: {
    close () {
      this.step = 1;  // 모달 처음으로 되돌리기
      this.resetValidation();  // 유효성검사 제거
      this.isVerified = null;
      this.mailCheck = null;
      this.$emit('close');
    },
    stepPlus() {
      if (this.step === 1) {
        this.inputData.id = this.email;
      }
      else{
        this.inputData.pw = this.pw;
      }
      this.step += 1;
    },
    resetValidation() {
      this.$refs.form.reset();  
    },
    validate(){
      this.$refs.form.validate();
    },
    emailVerify(){
      if (this.verificationWord == this.AuthorWord){
        this.isVerified = true;
      }else{
        this.isVerified = false;
      }
    },
    emailValid(){
      if (/.+@.+\..+/.test(this.email))
      {
        this.mail = true;
      } else{
        this.mail = false;
      }
    },
    checkEmail(){
      this.loading = true;
      axios.get(SERVER.URL + '/findPw/sendEmail/'+this.email)
        .then(res => {
          if(res.data === 'fail'){
            this.mailCheck = true;
          }else{
            this.AuthorWord = res.data;
            console.log(this.AuthorWord);
            this.mailCheck = false;
          }
          this.loading = false;
        })
        .catch(err => console.log(err));
    },
    changePw(){
      axios.post(SERVER.URL + '/findPw/newPw', this.inputData)
        .catch((err) => console.log(err));
    }
  },
  computed: {
    passwordConfirmRules () {
      return (this.pw === this.passwordConfirm) || Array('패스워드가 일치하지 않습니다.');
    },
    currentTitle(){
      switch(this.step){
      case 1: return 'JMT 패스워드 찾기';
      case 2: return '비밀번호 재설정';
      default: return '비밀번호 재설정 완료';
      }
    },
  },
  watch: {
    email(){
      this.emailValid();
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
