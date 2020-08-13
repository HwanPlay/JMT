<template>
  <div class="text-center">
    <v-dialog v-model="dialog" width="500">
      <template v-slot:activator="{ on, attrs }">
        <v-btn class="text-center" text small color="error" v-bind="attrs" v-on="on">Delete Account</v-btn>
      </template>

      <v-card>
        <v-card-title class="headline grey lighten-2">DELETE ACCOUNT</v-card-title>

        <v-card-text>Input Your Password</v-card-text>
        <v-card-text>
          <div class="text--primary">
          logout 됩니다.
          {{ alert_text }}
          </div>
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
      alert_text: '',
    };
  },
  methods: {
    SubmitDeleteAccount() {
      // this.dialog = false;
      this.clearPassword();
      axios
        .delete(SERVER.URL + '/user/delUser/' + this.$store.state.userId)
        .then((res) => {
          if (res.data === 'success') {
            // 로그아웃 하기.
            this.dialog = false;
            this.$store.dispatch('logout');
          } else {
            this.alert_text = '비밀번호가 틀렸습니다. 다시 입력하세요.';
            // 비번 틀렸다고 보여주기.
          }
        })
        .catch();
    },
    clearPassword() {
      this.password = '';
    }
  },
};
</script>

<style scoped>
.btn-box {
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>