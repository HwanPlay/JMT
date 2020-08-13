<template>
  <div >
    <div class="btn-box">
      <v-btn class="text-center" @click="showForm = !showForm" text small color="error">Password Change</v-btn>
    </div>

    <b-alert
      :show="dismissCountDown"
      dismissible
      :variant="alertColor"
      @dismissed="dismissCountDown=0"
      @dismiss-count-down="countDownChanged"
    >
      {{alertMessage}}
      <br/>
      This alert will dismiss after {{ dismissCountDown }} seconds...
    </b-alert>

    <div class="password-box" v-if="showForm" >
      <ProfilePasswordForm    
        @onClosePassword="ClosePassword"     
        @onSubmitSuccess="submitSuccess" 
        @onSubmitWrongPassword="submitWrongPassword"
        @onSubmitDifferentPassword="submitDifferentPassword"
      />
    </div>
  </div>
</template>

<script>
import ProfilePasswordForm from './ProfilePasswordForm.vue';

export default {
  name: 'ProfilePassword',
  data() {
    return {
      showForm: false,
      dismissSecs: 5,
      dismissCountDown: 0,
      alertColor: '',
      alertMessage: '',
    };
  },
  components: {
    ProfilePasswordForm,
  },
  methods: {
    ClosePassword() {
      this.showForm = false;
    },
    submitWrongPassword() {
      this.alertColor = 'danger';
      this.alertMessage = 'password가 틀렸습니다.';
      this.showAlert();
    },
    submitSuccess() {
      this.alertColor = 'success';
      this.alertMessage = '비밀번호를 변경했습니다.';
      this.showAlert();
      this.ClosePassword();
    },
    submitDifferentPassword() {
      this.alertColor = 'danger';
      this.alertMessage = 'Check Password';
      this.showAlert();
    },
    countDownChanged(dismissCountDown) {
      this.dismissCountDown = dismissCountDown;
    },
    showAlert() {
      this.dismissCountDown = this.dismissSecs;
    },
  }
};
</script>

<style>
.btn-box {
  display: flex;
  align-items: center;
  justify-content: center;
}
.password-box {
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>