<template>
  <v-row>
    <!-- 좌측 그룹 정보 부분 -->
    <v-col cols="6" style="vertical-align:middle; padding-top: 50px;">
      <v-row justify="center">
        <p
          v-if="groupInfo.hasMeeting"
          class="conferenceStatus"
          style="color: red; border: 2px solid red;"
        >회의 진행중</p>
        <p
          v-if="groupInfo.hasMeeting == false"
          class="conferenceStatus"
          style="color: green; border: 2px solid green;"
        >진행중인 회의가 없습니다.</p>
      </v-row>

      <!-- <v-row>
        <v-col cols="8">
          <h2>{{ groupInfo.groupName }}</h2>
        </v-col>

        <v-col cols="4">

          <v-btn @click="startMeeting" v-if="(groupInfo.hostId === this.$store.state.userId) && !groupInfo.hasMeeting" dark color="green">
            회의 시작
          </v-btn>
          <v-btn @click="joinMeeting" v-if="(groupInfo.hostId != this.$store.state.userId) && groupInfo.hasMeeting" dark color="blue darken-2">
            회의 참여
          </v-btn>
        </v-col>
      </v-row>-->

      
      <div id="conferenceBox">
        <v-row>
          <v-col cols="7">
            <h3 id="groupName">{{ groupInfo.groupName }}</h3>
          </v-col>

          <v-col cols="4">
            <v-btn
              @click="startMeeting"
              v-if="(groupInfo.hostId === this.$store.state.userId) && !groupInfo.hasMeeting"
              dark
              color="green"
            >
              회의 시작
              <!-- <router-link :to="{ name: 'Conference', params: { ??? }}">회의 시작</router-link> -->
            </v-btn>
            <v-btn
              @click="joinMeeting"
              v-if="(groupInfo.hostId != this.$store.state.userId) && groupInfo.hasMeeting"
              dark
              color="blue darken-2"
            >
              회의 참여
              <!-- <router-link :to="{ name: 'Conference', params: { ??? }}">회의 참여</router-link> -->
            </v-btn>
          </v-col>
        </v-row>

        <h4>호스트 : {{ groupInfo.hostName }}</h4>
        <div style="height:20px">
          <p>소개 : {{ groupInfo.groupIntro }}</p>
        </div>
      </div>
      <v-divider class="mb-10"></v-divider>

      <v-row>
        <v-col>
          <v-row>
            <v-col lg="5" xl="4">
              <h3>그룹원</h3>
            </v-col>
            <v-col lg="7" xl="8">
              <div v-if="groupInfo.hostId === this.$store.state.userId" style="float:right;">
                <InviteMember
                  :groupNo="groupInfo.groupNo"
                  :groupName="groupInfo.groupName"
                  :hostId="groupInfo.hostId"
                />
              </div>
            </v-col>
          </v-row>

          <v-card outlined>
            <v-col>
              <div v-if="members.length === 0">그룹원이 없습니다</div>
              <v-card-text v-for="memberInfo in members.slice(0,3)" :key='memberInfo.id' style="padding: 5px;">
                <MemberCard :userInfo = memberInfo />
              </v-card-text>

              <v-card-actions>
                <v-spacer></v-spacer>
                <GroupMembers
                  :membersInfo="members"
                  @refresh="getGroupMembers"
                  :groupNo="groupInfo.groupNo"
                  :hostId="groupInfo.hostId"
                />
              </v-card-actions>
            </v-col>
          </v-card>
        </v-col>
      </v-row>

      <v-col>
        <v-row justify="end">
          <div class="mr-2" v-if="groupInfo.hostId === this.$store.state.userId">
            <v-btn dark color="red" @click="onModal=true">그룹 관리</v-btn>
            <v-dialog v-model="onModal" max-width="500px">
              <EditGroup @close="onModal=false" :groupInfo="groupInfo" />
            </v-dialog>
          </div>
          <v-btn
            dark
            color="red"
            @click="exitGroup"
            v-if="groupInfo.hostId !== this.$store.state.userId"
          >그룹 탈퇴</v-btn>
        </v-row>
      </v-col>
    </v-col>

    <v-spacer></v-spacer>
    <!-- 우측 캘린더 부분 -->
    <v-col cols="5">
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
    EditGroup
  },
  props: {
    groupInfo: Object
  },
  data: () => ({
    onModal: false,
    members: [],
    sock: null,
    ws: null,
    reconnect: 0,
    token: '',
    meetingNo: null
  }),
  methods: {
    exitGroup() {
      axios
        .delete(
          SERVER.URL +
            '/groupmember/delno/' +
            this.groupInfo.groupNo +
            '/' +
            this.$store.state.userId
        )
        .then(res => {
          console.log(res);
          this.$router.push('/Home');
        })
        .catch(err => console.log(err.response));
    },

    changeHasMeeting() {
      var tmp = null;
      axios
        .put(SERVER.URL + '/group/hasmeeting/' + this.groupInfo.groupNo)
        .then(res => {
          tmp = res.data.hasMeeting;
        })
        .finally(() => {
          this.send(tmp);
        });
    },

    getGroupMembers() {
      axios
        .get(SERVER.URL + '/groupmember/getno/' + this.groupInfo.groupNo)
        .then(res => {
          this.members = res.data.groupMembers;
        })
        .catch(err => console.log(err.response));
    },

    startMeeting() {
      this.changeHasMeeting();
      axios
        .post(SERVER.URL + '/meeting/add', {
          groupNo: this.groupInfo.groupNo,
          title: this.$store.state.myName + '\'s Meeting'
        })
        .then(res => {
          this.meetingNo = res.data.meetingNo;
          this.$router.push({name: 'Conference',
            params: { roomId : this.groupInfo.roomId, hostId : this.groupInfo.hostId },
            query: { groupNo: this.groupInfo.groupNo, groupName: this.groupInfo.groupName, meetingNo:this.meetingNo }});
        });
    },

    joinMeeting(){
      axios.get(SERVER.URL + '/meeting/get/currentmeeting/'+this.groupInfo.groupNo)
        .then(res => {
          this.$router.push({name: 'Conference', 
            params: { roomId : this.groupInfo.roomId, hostId : this.groupInfo.hostId },
            query: { groupNo: this.groupInfo.groupNo, groupName: this.groupInfo.groupName, meetingNo: this.meetingNo }
          });
        });
    },

    send(tmp) {
      const msg = {
        meeting: tmp,
        groupNo: this.groupInfo.groupNo
      };
      this.ws.send('/meeting', JSON.stringify(msg), {
        token: this.$store.state.accessToken
      });
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
    // this.connect();
  },


  watch: {
    groupInfo() {
      this.getGroupMembers();
    }
  }
};
</script>


<style scoped>
#conferenceBox {
  margin-top: 30px;
  margin-bottom: 40px;
  margin-left: 10px;
  background-image: linear-gradient(
    -20deg,
    #c4c0cf 0%,
    #b8c2e0 100%,
    #9e9eec 100%
  );
  border-radius: 15px;
  padding: 15px;
  box-shadow: 2px 1px 7px 3px rgb(167, 167, 167);
}

.conferenceStatus {
  padding-left: 10px;
  padding-right: 10px;
  font-size: 30px;
  border-radius: 10px;
}
</style>