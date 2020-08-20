<template>
  <div class="text-center">
    <v-dialog v-model="dialog" width="30%">
      <template v-slot:activator="{ on, attrs }">
        <!-- <v-btnclass="p-1 text-center" text small color="error" v-bind="attrs" v-on="on">비밀번호 변경</v-btnclass=> -->
        <div class="text-center">
          <v-btn rounded outlined color="red" @click="initPass" v-bind="attrs" v-on="on" dark>비밀번호 변경</v-btn>
        </div>
      </template>

      <v-card>
        <h2 class="py-3 d-flex justify-center profile-title">비밀번호 변경</h2>

        <div class="password-box">
          <ProfilePasswordForm
            @onClosePassword="ClosePassword"
            @onSubmitSuccess="submitSuccess"
            :initbool="initbool"
          />
        </div>

        
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
      initbool: false,
    };
  },
  components: {
    ProfilePasswordForm,
  },
  methods: {
    ClosePassword() {
      this.dialog = false;
    },
    initPass() {
      this.initbool = !this.initbool;
    },
    submitSuccess() {
      this.ClosePassword();
      this.$emit('onSubmitSuccess');
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