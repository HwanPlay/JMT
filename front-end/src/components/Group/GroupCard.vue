<template>
  <v-window v-model="step">
    <hr>
    <v-window-item :value="1">
      <v-list-item>
        <v-list-item-content>
          <v-list-item-title>{{ groupInfo.groupName }}</v-list-item-title>
        </v-list-item-content>
        <div>
          <v-btn color='warning' style='outline: none;' @click='step++'>회의 시작</v-btn>
        </div>
      </v-list-item>
    </v-window-item>
    <v-window-item :value="2">
      <v-list-item>
        <v-text-field style="width: 50%" v-model="meetingTitle" label="회의 명" required @keypress.enter="startMeeting"></v-text-field>
        <v-btn text color='rgb(255, 128, 74)' style='outline: none;' @click='startMeeting'>회의 시작</v-btn>
      </v-list-item>
    </v-window-item>
  </v-window>
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
    meetingModalOn: Boolean,
  },
  data(){
    return{
      step: 1,
      ws: null,
      sock : null,
      recvList: [],
      tmp_meeting: false,
      meetingNo: null,
      meetingTitle: this.$store.state.myName + '의 회의',
      sModal: false,
    };
  },
  methods:{
    send(tmp) {
      const msg = {
        meeting : tmp,
        groupNo : this.groupInfo.groupNo
      };
      this.ws.send('/meeting', JSON.stringify(msg), {'token' : this.$store.state.accessToken});
    },

    changeHasMeeting(){
      var tmp = null;
      axios.put(SERVER.URL+'/group/hasmeeting/'+this.groupInfo.groupNo)
        .then(res => {
          tmp = res.data.hasMeeting;
        })
        .finally( () => {
          this.send(tmp);
        });
    },

    startMeeting(){
      this.changeHasMeeting();
      axios.post(SERVER.URL + '/meeting/add', {groupNo: this.groupInfo.groupNo, title: this.$store.state.myName+'\'s Meeting'})
        .then(res => {
          this.meetingNo = res.data.meetingNo;
          this.$router.push({ name: 'Conference',
            params: {
              roomId: this.groupInfo.roomId,
              hostId: this.groupInfo.hostId,
              groupNo: this.groupInfo.groupNo,
              groupName: this.groupInfo.groupName,
            },
            query: { meetingNo: this.meetingNo , nickname: this.$store.state.myName},
          });
        });
      // this.$router.push({ name: 'Conference',
      //   params: { roomId: this.groupInfo.roomId },
      //   query: { groupNo: this.groupInfo.groupNo, groupName: this.groupInfo.groupName }
      // });
    },

    connect() { 
      this.ws.connect({'token' : this.$store.state.accessToken}, () => {
      }, () => {
        if(this.reconnect++ <= 5) {
          setTimeout(()=> {
            console.log('connection reconnect');
            this.sock = new SockJS(SERVER.URL2);
            this.ws = Stomp.over(this.sock);
            this.connect();
          }, 10*1000);
        }
      });
    },

  },
  created() {
    this.sock = new SockJS(SERVER.URL2);
    this.ws = Stomp.over(this.sock);
  },

  mounted(){
    this.step = 1;
    this.connect();
  },
  destroyed() {
    this.ws.disconnect();  
  },

  watch:{
    meetingModalOn(){
      this.step = 1;
    }
  }
};
</script>

<style>
  .top {
    background-color: rgb(52, 63, 87);
    color: white;
  }
</style>