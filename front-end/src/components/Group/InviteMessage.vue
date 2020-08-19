<template>
  <v-card class="mx-auto animate__animated animate__headShake" width="400px" height="450px">
    <v-card-title class="top justify-center align-content-center">그룹 초대 메세지</v-card-title>
    <v-container>
    <v-card-text class="mb-5">
      <h5>To. {{ $store.state.myName }}님</h5>
      <h4>제 그룹으로 초대합니다!</h4>
      <h4>수락해주세요.</h4>
      <br>
      <h5 class="d-flex justify-end">{{ message.groupName }} 그룹</h5>
      <h6 class="d-flex justify-end">From. {{ message.sender }}</h6>
    </v-card-text>
      <v-btn class="mt-4" color="rgb(255, 128, 74)" block style="outline:none" @click="acceptInvite">
        <div style="color: white">수락</div>
      </v-btn>
      <v-btn class="mt-4" color="error" block style="outline:none" @click="removeRequest">
        <div style="color: white">거절</div>
      </v-btn>
    </v-container>
  </v-card>
</template>

<script>
import axios from 'axios';
import SERVER from '../../api/spring.js';

export default {
  name: 'InviteMessage',
  props: {
    message: Object
  },
  data(){
    return {
      step: 1
    };
  },
  methods:{
    acceptInvite(){
      const Info = {
        groupNo: this.message.groupNo,
        id: this.$store.state.userId,
        nickname: this.$store.state.myName
      };
      axios.post(SERVER.URL + '/groupmember/add', Info);
      this.removeRequest();
    },
    removeRequest(){
      axios.delete(SERVER.URL + '/request/del/' + this.message.receiver + '/' + this.message.sender + '/' + this.message.groupNo)
        .then(() => {
          this.$emit('done');
          console.log('deleted!!!!!!!!');
        })
        .catch(err => console.log(err));
    }
  }
};
</script>

<style>
  .top {
    background-color: white;
    color: rgb(52, 63, 87);
  }
</style>