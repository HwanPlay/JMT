<template>
  <v-window v-model="step">
    <v-window-item :value="1">
      <v-list-item>
        <v-list-item-content>
          <v-list-item-title>{{ Request.groupName }} 그룹</v-list-item-title>
          <v-list-item-subtitle>{{ Request.hostId }}님의 초대</v-list-item-subtitle>
        </v-list-item-content>
        <v-btn text color='rgb(255, 128, 74)' style='outline: none;' @click="step++">수락</v-btn>
        <v-btn text color='danger' style='outline: none' @click='removeRequest'>거절</v-btn>
      </v-list-item>
    </v-window-item>
    <v-window-item :value="2">
      <v-list-item>
        <v-text-field v-model="nickname" label="닉네임" required @keypress.enter="acceptInvite"></v-text-field>
        <v-btn text color='error' style='outline: none;' @click='acceptInvite'>가입</v-btn>
      </v-list-item>
    </v-window-item>
  </v-window>
</template>

<script>
import axios from 'axios';
import SERVER from '../../api/spring.js';

export default {
  name: 'RequestCard',
  props: {
    Request: Object,
    dialog: Boolean,
  },
  data(){
    return {
      nickname: this.$store.state.myName,
      step: 1
    };
  },
  methods:{
    acceptInvite(){
      const Info = {
        groupNo: this.Request.groupNo,
        id: this.$store.state.userId,
        nickname: this.nickname
      };
      axios.post(SERVER.URL + '/groupmember/add', Info);
      this.removeRequest();
    },
    removeRequest(){
      axios.delete(SERVER.URL + '/request/delno/' + this.Request.requestNo)
        .then(() => {
          this.$el.parentNode.removeChild(this.$el);
        });
    }
  },
  watch:{
    dialog(){
      this.step = 1;
    }
  }
};
</script>

<style>

</style>