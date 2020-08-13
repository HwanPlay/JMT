<template>
  <v-card class="mx-auto" tile>
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

    <v-container>
      <v-row align="start" class="fill-height">
        <v-col align-self="start" class="pa-0" cols="4">
          <v-avatar class="profile"  size="164" tile>
            <img src="../../assets/profile/blank-profile.png" alt="사진 자리 ㅠㅠ">
          </v-avatar>
        </v-col>
        <v-col class="py-0">
          <v-list-item>
            <v-list-item-content>
              <v-list-item-title class="title my-3">Email: {{this.$store.state.userId}}</v-list-item-title>
              <v-form ref="form">
                <v-text-field v-model="user_name" :counter="max" :rules="rules" label="User Name"></v-text-field>
              </v-form>
            </v-list-item-content>
          </v-list-item>
          <v-divider inset></v-divider>
          <div class="my-2 save-btn">
            <v-btn @click="submitSave" text color="#526387" class="align-self-end">Save</v-btn>
          </div>
        </v-col>
      </v-row>
    </v-container>
  </v-card>
</template>

<script>
import axios from 'axios';
import SERVER from '../../api/spring.js';

export default {
  data() {
    return {
      max: 10,
      user_name: this.$store.state.myName,
      alertColor: '',
      alertMessage: '',
      dismissCountDown: 0,
      dismissSecs: 5,
    };
  },
  computed: {
    rules() {
      const rules = [];
      if (this.max) {
        const rule = (v) =>
          (v || '').length <= this.max ||
          `A maximum of ${this.max} characters is allowed`;
        rules.push(rule);
      }
      return rules;
    },
  },
  watch: {
    max: 'validateField',
    model: 'validateField',
  },
  methods: {
    validateField() {
      this.$refs.form.validate();
    },
    submitSave() {
      axios
        .post(SERVER.URL + '/user/modify', {
          name: this.user_name,
        })
        .then((res) => {
          if(res.data === 'success'){
            console.log(res);
            this.makeAlert({'alertColor': 'primary', 'alertMessage': 'Success'});
          } else {
            console.log(res);
            this.makeAlert({'alertColor': 'danger', 'alertMessage': 'fail to edit'});
          }
        })
        .catch(err=>console.error(err));
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

<style  scoped>
.save-btn {
  display: flex;
  align-items: center;
  justify-content: flex-end;
}
</style>