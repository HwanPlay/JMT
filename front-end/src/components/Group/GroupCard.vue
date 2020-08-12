<template>
  <div>
    <v-list-item>
      <v-list-item-content>
        <v-list-item-title>{{ groupInfo.groupName }}</v-list-item-title>
      </v-list-item-content>
      <div>
        <v-btn color='warning' style='outline: none;' @click='startMeeting'>회의 시작</v-btn>
      </div>
    </v-list-item>
  </div>
</template>

<script>
import axios from 'axios';
import SERVER from '../../api/spring.js';

import SockJS from 'sockjs-client';
import Stomp from 'webstomp-client';

export default {
  name: 'GroupCard',
  props: {
    groupInfo: Object,
  },
  data(){
    return{
      ws: null,
      recvList: [],
      tmp_meeting: false,
      meetingNo: null,
    };
  },
  methods:{
    send(){
      const msg = {
        isMeeting: this.tmp_meeting,
        groupNo : this.groupInfo.groupNo
      };
      this.ws.send('/meeting', JSON.stringify(msg), {'token': this.$store.state.accessToken});
    },

    changeHasMeeting(){
      axios.put(SERVER.URL + '/group/hasmeeting/' + this.groupInfo.groupNo)
        .then(res => {
          this.tmp_meeting = res.data.hasMeeting;
        })
        .finally(() => {
          this.send();
        });
    },

    startMeeting(){
      this.changeHasMeeting();
      // axios.post(SERVER.URL + '/meeting/adds')
      //   .then(res => {
      //     this.meetingNo = res.meetingNo;
      //     this.$router.push({ name: 'Conference',
      //       params: { roomId: this.groupInfo.roomId },
      //       query: { groupNo: this.groupInfo.groupNo, groupName: this.groupInfo.groupName }
      //     })
      // })
      this.$router.push({ name: 'Conference',
        params: { roomId: this.groupInfo.roomId },
        query: { groupNo: this.groupInfo.groupNo, groupName: this.groupInfo.groupName }
      });
    },

    connect(param) {
      this.ws.connect({'token' : this.$store.state.accessToken}, frame => {
        console.log('소켓 연결 성공', frame);
        this.ws.subscribe('/send/meeting/' + this.groupInfo.groupNo, res => {
          console.log('구독으로 받은 메세지 입니다', res.body);
          this.recvList.push(JSON.parse(res.body));
          console.log(this.recvList);
        });
      });
    },

  },
  created() {
    this.sock = new SockJS(SERVER.URL2);
    this.ws = Stomp.over(this.sock);
  },

  mounted(){
    this.connect();
  }
};
</script>

<style>

</style>