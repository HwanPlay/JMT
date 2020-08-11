<template>
  <div>
    <v-row>
      <v-col cols=5></v-col>
      <v-col cols=2>
        <v-btn class="text-center" @click="showForm = !showForm" text small color="error">Password Change</v-btn>
      </v-col>
      <v-col cols=5></v-col>
    </v-row>

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

    <div v-if="showForm">
      <ProfilePasswordForm 
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
    submitWrongPassword() {
      this.alertColor = 'danger';
      this.alertMessage = 'password가 틀렸습니다.';
      this.showAlert();
    },
    submitSuccess() {
      this.alertColor = 'success';
      this.alertMessage = '비밀번호를 변경했습니다.';
      this.showAlert();
      this.showForm = false;
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

</style>