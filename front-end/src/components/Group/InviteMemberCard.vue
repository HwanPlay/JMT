<template>
  <v-list-item>
    <v-list-item-avatar color="grey" size="55"> <v-img :src="'http://joinmeeting.tk/images/'+user.profile_img"></v-img></v-list-item-avatar>
    <v-list-item-content>
      <v-list-item-title class="headline">{{ user.name }}</v-list-item-title>
      <v-list-item-subtitle>{{ user.id }}</v-list-item-subtitle>
    </v-list-item-content>
    <div v-if='!isInvited' class="my-2">
      <v-btn  color="rgb(52, 63, 87)" dark style="outline: none;" @click="sendRequest">
        추가
      </v-btn>
    </div>
  </v-list-item>
</template>

<script>
import axios from 'axios';
import SERVER from '../../api/spring.js';
import SockJS from 'sockjs-client';
import Stomp from 'webstomp-client';


export default {
  name: 'InviteMemberCard',
  props: {
    user: Object,
    groupNo: Number,
    groupName: String,
    hostId: String
  },
  data(){
    return {
      isInvited: false,
      ws : null,
      sock : null,
    };
  },
  methods:{
    sendRequest() {
      const info = {
        groupNo : this.groupNo,
        hostId : this.hostId,
        userId : this.user.id,
        groupName : this.groupName
      };
      axios.post(SERVER.URL + '/request/send', info)
        .then(res => {
          this.$emit('send');
          this.isInvited = true;
          this.$el.parentNode.removeChild(this.$el);
          this.send(this.user, res.data.requestNo);
        })
        .catch(err => {
          this.$emit('fail');
          console.log(err.response);
        });
    },

    connect() {
      console.log('소켓 연결 시도합니다 . 서버 주소 : ' + SERVER.URL2 + '/ws' );
      this.ws.connect({'token' : this.$store.state.accessToken}, frame => {
        console.log('소켓 연결 성공', frame);
      });
    },

    send(userInfo, requestNo) {
      const msg = {
        sender : this.hostId,
        receiver : userInfo.id,
        groupNo : this.groupNo,
        groupName : this.groupName,
        requestNo: requestNo
      };
      this.ws.send('/request', JSON.stringify(msg), {'token' : this.$store.state.accessToken});
    }
  },

  created() {
    this.sock = new SockJS(SERVER.URL2);
    this.ws = Stomp.over(this.sock);
  },

  mounted() {
    this.connect();
  }
};

</script>

<style>

</style>