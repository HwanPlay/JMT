<template>
  <v-card class="mx-auto animate__animated animate__headShake" width="100%">
    <v-card-title class="top justify-center align-content-center">그룹 초대 메세지</v-card-title>
    <v-card-text>
      <v-list-item-title>{{ message.sender }}님의 초대</v-list-item-title>
      <v-list-item-subtitle>{{ message.groupName }} 그룹</v-list-item-subtitle>
    </v-card-text>
    <v-card-actions>
      <v-btn text style="outline:none" color="rgb(52, 63, 87)" @click='removeRequest'>
        거절
      </v-btn>
      <v-spacer></v-spacer>
      <v-btn text style="outline:none" color="rgb(255, 128, 74)" @click='acceptInvite'>
        수락
      </v-btn>
    </v-card-actions>
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