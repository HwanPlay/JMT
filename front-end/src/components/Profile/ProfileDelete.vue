<template>
  <div class="text-center">
    <v-dialog v-model="dialog" width="50%">
      <template v-slot:activator="{ on, attrs }">
        <v-btn class="text-center" text small color="error" v-bind="attrs" v-on="on">Delete Account</v-btn>
      </template>

      <v-card>

        <b-alert
          class="alert_pos"
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


        <v-card-title class="headline grey lighten-2">DELETE ACCOUNT</v-card-title>

        <v-card-text>
          비밀번호를 입력해 주세요.
          <br>
          logout 됩니다.
        </v-card-text>
        

        <v-card-text>
          <v-text-field type="password" v-model="password" label="Password" required></v-text-field>
        </v-card-text>

        <v-divider></v-divider>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" text @click="SubmitDeleteAccount">Submit</v-btn>
          <v-btn color="red" text @click="dialog = false">Cancel</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import axios from 'axios';
import SERVER from '../../api/spring.js';

export default {
  name: 'ProfileDelete',
  data() {
    return {
      dialog: false,
      password: '',
      
      alertColor: '',
      alertMessage: '',
      dismissCountDown: 0,
      dismissSecs: 5,
    };
  },
  methods: {
    SubmitDeleteAccount() {
      // this.dialog = false;
      axios
        .delete(SERVER.URL + '/user/delUser/' + this.$store.state.userId)
        .then((res) => {
          console.log(res);
          // if (res.data === 'success') {
          //   // 로그아웃 하기.
          //   this.dialog = false;
          //   this.$store.dispatch('logout');
          // } else {
          //   this.makeAlert({alertColor: 'danger', alertMessage:'비밀번호가 틀렸습니다. 다시 입력하세요'});
          //   // 비번 틀렸다고 보여주기.
          // }
        })
        .catch(err=>console.error(err));
      this.clearPassword();
    },
    clearPassword() {
      this.password = '';
    },
    makeAlert(props) {
      this.alertColor = props.alertColor;
      this.alertMessage = props.alertMessage;
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
.alert_pos {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
}
</style>