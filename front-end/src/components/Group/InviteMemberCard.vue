<template>
  <v-list-item>
    <v-list-item-avatar :src="require(`../../assets/profile/profile-1.png`)" color="grey" size="55"> <v-img :src="require('../../assets/profile/profile1.jpg')"></v-img></v-list-item-avatar>
    <v-list-item-content>
      <v-list-item-title class="headline">{{ user.name }}</v-list-item-title>
      <v-list-item-subtitle>{{ user.id }}</v-list-item-subtitle>
    </v-list-item-content>
    <div v-if='!isInvited' class="my-2">
      <v-btn  color="rgb(52, 63, 87)" dark style="outline: none;" @click="addMember(user)">
        추가
      </v-btn>
    </div>
    <div v-else class="my-2">
      <v-btn :disabled='true' color="rgb(52, 63, 87)" dark style="outline: none;" @click="addMember(user)">
        완료
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
    groupNo: Number
  },
  data(){
    return {
      isInvited: false,
      ws : null,
      sock : null,
    };
  },
  methods:{
    addMember(userInfo){
      const info = {
        groupNo: this.groupNo,
        id: userInfo.id,
        nickname: userInfo.name
      };
      axios.post(SERVER.URL + '/groupmember/add', info)
        .then((res) => {
          console.log('newmember :', res);
          this.isInvited = true;
        })
        .catch((err) => 
          console.log(err.response));
    },

    connect() {
      console.log('소켓 연결 시도합니다 . 서버 주소 : ' + SERVER.URL2 + '/ws' );
      this.ws.connect({'token' : this.$store.state.accessToken}, frame => {
        console.log('소켓 연결 성공', frame);
      });
    },

    send(userInfo) {
      const msg = {
        receiver : userInfo.id,
        groupNo : this.groupNo
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