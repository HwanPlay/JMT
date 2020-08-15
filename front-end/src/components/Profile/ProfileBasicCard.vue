<template>
  <v-card class="mx-auto" tile>
    <v-container>
      <v-row align="start" class="fill-height">
        <v-col align-self="start" class="pa-0" cols="4">
          <v-avatar class="profile"  size="164" tile>
            <img :src="'http://joinmeeting.tk'+this.$store.state.myPicture" alt="사진 자리 ㅠㅠ">
          </v-avatar>
        </v-col>
        <v-col cols="8" class="py-0">
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
            <input id="file" type="file" ref="file" accept="img/*" v-on:change="fileSelect()">
            <v-btn @click="submitSave" text color="#526387" class="align-self-end">Save</v-btn>
          </div>
        </v-col>
      </v-row>
    
    </v-container>
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

      file:''
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
    fileSelect() {
      console.log(this.$refs);
      this.file = this.$refs.file.files[0];
      console.log(this.file);
    },
    submitSave() {
      let formData = new FormData();

      formData.append('name', this.user_name);
      formData.append('multipartFile', this.file);


      console.log('size:', this.file.size);
      // for(let [name, value] of formData) {
      //   console.log(`${name} = ${value}`); // key1=value1, then key2=value2
      // }
      // console.log(formData);
      
      if (this.file.size > 3e6) {
        this.makeAlert({'alertColor': 'danger', 'alertMessage': 'Please choose less than 3MBYTE'});
      } else {
        // file upload 가능
        axios
          .post(SERVER.URL + '/user/modify',formData, {
            headers: {
              'Content-Type': 'multipart/form-data'
            }
          })
          .then((res) => {
            console.log(res);
            if(res.status === 200){
              this.$store.commit('SET_MY_PROFILE', res);

              this.makeAlert({'alertColor': 'primary', 'alertMessage': 'Success'});
            } else {
              this.makeAlert({'alertColor': 'danger', 'alertMessage': 'fail to edit'});
            }
          })
          .catch(err=>console.error(err));
      }

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
.alert_pos {
  position: absolute;
  z-index: 9999;
  bottom: 0;
  left: 0;
  width: 100%;
}
</style>