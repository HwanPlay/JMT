<template>
  <div>
    <v-list-item>
      <v-list-item-content>
        <v-list-item-title>{{ groupInfo.groupName }}</v-list-item-title>
      </v-list-item-content>
      <div>
        <v-btn color='warning' style='outline: none;' @click='sModal=true'>회의 시작</v-btn>
      </div>
        <v-dialog v-model="sModal" width="500px">
          <v-card width="500px">
            <v-card-title class="top">회의 시작하기</v-card-title>
            <v-container>
              <v-form ref="form" width="500px;" lazy-validation class="ml-2 mr-2">
                <v-text-field v-model="meetingTitle" label="회의 명" required></v-text-field>
                <v-card-actions>
                  <v-spacer></v-spacer>
                  <v-btn :disabled="!!!meetingTitle" text color="error" class="mr-4" @click="startMeeting">회의 시작</v-btn>
                </v-card-actions>
              </v-form>
            </v-container>
          </v-card>
        </v-dialog>
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
      sock : null,
      recvList: [],
      tmp_meeting: false,
      meetingNo: null,
      meetingTitle: this.$store.state.myName + '의 회의',
      sModal: false
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
          console.log('good', res); 
          this.meetingNo = res.data.meetingNo;
          this.$router.push({ name: 'Conference',
            params: { roomId: this.groupInfo.roomId },
            query: { groupNo: this.groupInfo.groupNo, groupName: this.groupInfo.groupName, meetingNo: this.meetingNo }
          });
        });
      // this.$router.push({ name: 'Conference',
      //   params: { roomId: this.groupInfo.roomId },
      //   query: { groupNo: this.groupInfo.groupNo, groupName: this.groupInfo.groupName }
      // });
    },

    connect() { 
      this.ws.connect({'token' : this.$store.state.accessToken}, frame => {
        console.log('소켓 연결 성공', frame);
        this.ws.subscribe('/send/meeting/' + this.groupInfo.groupNo, res => {
          this.recvList.push(res.body);
          console.log('받은 데이터' + this.recvList);
        });
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
    this.connect();
  }
};
</script>

<style>

</style>