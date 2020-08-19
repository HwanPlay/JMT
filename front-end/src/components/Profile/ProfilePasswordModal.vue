<template>
  <div class="text-center">
    <v-dialog v-model="dialog" width="30%">
      <template v-slot:activator="{ on, attrs }">
        <!-- <v-btnclass="p-1 text-center" text small color="error" v-bind="attrs" v-on="on">비밀번호 변경</v-btnclass=> -->
        <div class="text-center">
          <v-btn rounded outlined color="red" v-bind="attrs" v-on="on" dark>비밀번호 변경</v-btn>
        </div>
      </template>

      <v-card>
        <h2 class="py-3 d-flex justify-center profile-title">비밀번호 변경</h2>

        <div class="password-box">
          <ProfilePasswordForm
            @onClosePassword="ClosePassword"
            @onSubmitSuccess="submitSuccess"
            @onSubmitWrongPassword="submitWrongPassword"
            @onSubmitDifferentPassword="submitDifferentPassword"
          />
        </div>

        <b-alert
          :show="dismissCountDown"
          dismissible
          :variant="alertColor"
          @dismissed="dismissCountDown=0"
          @dismiss-count-down="countDownChanged"
        >
          {{alertMessage}}
          <br />
          This alert will dismiss after {{ dismissCountDown }} seconds...
        </b-alert>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import axios from 'axios';
import SERVER from '../../api/spring.js';
import ProfilePasswordForm from './ProfilePasswordForm.vue';

export default {
  name: 'ProfileDelete',
  data() {
    return {
      dialog: false,
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
      this.dialog = false;
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
  },
};
</script>

<style scoped>
.btn-box {
  display: flex;
  align-items: center;
  justify-content: center;
}
.profile-title {
  background-color: rgb(14, 23, 38);
  color: white;
}
</style>