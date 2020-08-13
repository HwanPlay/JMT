<template>
  <v-row>
        {{ groupInfo }}
    <!-- 좌측 그룹 정보 부분 -->
    <v-col cols="5">
      <v-row justify="center">
        <v-btn v-if="!groupInfo.hasMeeting" dark text color="green darken-1">회의 진행중이 아닙니다</v-btn>
        <v-btn v-if="groupInfo.hasMeeting" dark text color="green darken-1">회의 진행중</v-btn>
      </v-row>

      <v-row>
        <v-col cols="8">
          <h2>{{ groupInfo.groupName }}</h2>
        </v-col>

        <v-col cols="4">

          <v-btn @click="startMeeting" v-if="(groupInfo.hostId === this.$store.state.userId) && !groupInfo.hasMeeting" dark color="green">
            회의 시작
            <!-- <router-link :to="{ name: 'Conference', params: { ??? }}">회의 시작</router-link> -->
          </v-btn>
          <v-btn @click="joinMeeting" v-if="(groupInfo.hostId != this.$store.state.userId) && groupInfo.hasMeeting" dark color="blue darken-2">
            회의 참여
            <!-- <router-link :to="{ name: 'Conference', params: { ??? }}">회의 참여</router-link> -->
          </v-btn>
        </v-col>
      </v-row>
        
      <h4>Leader : {{ groupInfo.hostName }}</h4>
      <div style="height:60px">
        <p> Intro : {{ groupInfo.groupIntro }}</p>
      </div>

      <v-divider class="mb-10"></v-divider>
      
      <v-row>
        <v-col>

            <v-row>
              <v-col lg="5" xl="4">
                <h3>Members</h3>
              </v-col>
              <v-col lg="7" xl="8">
                <div v-if="groupInfo.hostId === this.$store.state.userId">
                  <InviteMember :groupNo = groupInfo.groupNo />
                </div>
              </v-col>
            </v-row>
            
          <v-card outlined>
            <v-col>
              <div v-if="members.length === 0">그룹원이 없습니다</div>
              <v-card-text v-for="(memberInfo, i) in members.slice(0,3)" :key=i style="padding: 5px;">
                <MemberCard :userInfo = memberInfo />
              </v-card-text>
              
              <v-card-actions>
                <v-spacer></v-spacer>
                <GroupMembers :membersInfo=members @refresh="getGroupMembers" :groupNo=groupInfo.groupNo :hostId=groupInfo.hostId />
              </v-card-actions>
            </v-col>
          </v-card>

        </v-col>
      </v-row>

      <v-col>
        <v-row justify="end">
          <div class="mr-2" v-if="groupInfo.hostId === this.$store.state.userId">
            <v-btn dark color="red" @click='onModal=true'>그룹 관리</v-btn>
            <v-dialog v-model="onModal" max-width="500px">
              <EditGroup @close="onModal=false" :groupInfo=groupInfo />
            </v-dialog>
          </div>
          <v-btn dark color="red" @click="exitGroup" v-if="groupInfo.hostId !== this.$store.state.userId">
            그룹 탈퇴
          </v-btn>
        </v-row>
      </v-col>
      
    </v-col>

    <v-spacer></v-spacer>
    <!-- 우측 캘린더 부분 -->
    <v-col cols="6">
      <GroupCalendar />
    </v-col>
  </v-row>
</template>

<script>
import SERVER from '../../api/spring.js';
import axios from 'axios';

import SockJS from 'sockjs-client';
import Stomp from 'webstomp-client';

import MemberCard from './MemberCard.vue';
import GroupMembers from './GroupMembers.vue';
import InviteMember from './InviteMember.vue';
import EditGroup from './EditGroup.vue';
import GroupCalendar from './GroupCalendar.vue';

export default {
  name: 'group',
  components: {
    MemberCard,
    GroupMembers,
    InviteMember,
    GroupCalendar,
    EditGroup,
  },
  props: {
    groupInfo: Object,
  },
  data: () => ({
    onModal: false,

    members : [],
    sock : null,
    ws : null,
    reconnect : 0,
    token : '',
    recvList : [],
    tmp_meeting : null,
    meetingNo: null,
  }),
  methods: {
    exitGroup(){
      axios.delete(SERVER.URL+'/groupmember/delno/'+this.groupInfo.groupNo+'/'+this.$store.state.userId)
        .then(res => { console.log(res);
          this.$router.push('/Home');
        })
        .catch(err => console.log(err.response));
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

    getGroupMembers(){
      axios.get(SERVER.URL+'/groupmember/getno/'+this.groupInfo.groupNo)
        .then(res => {
          this.members = res.data.groupMembers;
        })
        .catch(err => console.log(err.response));
    },

    startMeeting(){
      // this.changeHasMeeting();
      axios.post(SERVER.URL + '/meeting/add', {groupNo: this.groupInfo.groupNo, title: this.$store.state.myName+'\'s Meeting'})
        .then(res => {
          this.meetingNo = res.data.meetingNo;
          this.$router.push({name: 'Conference',
            params: { roomId : this.groupInfo.roomId }, 
            query: { groupNo: this.groupInfo.groupNo, groupName: this.groupInfo.groupName, meetingNo:this.meetingNo }});
        });
    },

    joinMeeting(){
      this.$router.push({name: 'Conference', params: { roomId : this.groupInfo.roomId }});
    },


    connect(param) { console.log(param);
      this.ws.connect({'token' : this.$store.state.accessToken}, frame => {
        console.log('소켓 연결 성공', frame);
        this.ws.subscribe('/send/meeting/' + param, res => {
          console.log('구독으로 받은 메세지 입니다', res.body);
          this.recvList.push(JSON.parse(res.body));
          console.log(this.recvList);
        });
      }, error => {
        if(this.reconnect++ <= 5) {
          setTimeout(()=> {
            console.log('connection reconnect');
            this.sock = new SockJS(SERVER.URL2);
            this.ws = Stomp.over(this.sock);
          });
        }
      });
    },


    send(tmp) {
      const msg = {
        meeting : tmp,
        groupNo : this.groupInfo.groupNo
      };
      this.ws.send('/meeting', JSON.stringify(msg), {'token' : this.$store.state.accessToken});
    }


  },

  created() {
    this.sock = new SockJS(SERVER.URL2);
    this.ws = Stomp.over(this.sock);

  },

  mounted() {
    // axios.get(SERVER.URL+'/groupmember/getno/'+this.groupInfo.groupNo)
    //   .then(res => {
    //     this.members = res.data.groupMembers;
    //   })
    //   .catch(err => console.log(err.response));
    this.connect();
  },


  watch:{
    groupInfo(){
      this.getGroupMembers();
    }
  }
};
</script>
