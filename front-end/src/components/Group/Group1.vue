<template>
  <v-row style="height: 100%; width: 100%;">
    {{ groupInfo }}
    <!-- 좌측 그룹 정보 부분 -->
    <v-col cols="7" style="width: 100%;">
      <v-row style="height: 15%; width: 100%; margin-left: 20px; margin-top: 20px;">
        <v-col cols="8">
          <h2>{{ groupInfo.groupName }}</h2>
          <v-row>
            <v-col cols="2" class="p-0 ml-3">
              <v-img :src="require('../../assets/profile/profile1.jpg')" class="rounded-circle p-0" height="60" width="60"></v-img>
            </v-col>
            <v-row class="ml-0" justify="start" style="height: 100%;">
              <div>
                <span style="font-size: 20px;">{{ groupInfo.hostName }}</span>
                <br>
                <span style="font-size: 16px;">{{ groupInfo.hostId }}</span>
              </div>
            </v-row>
          </v-row>
        </v-col>
        <v-col cols="4">
          <v-btn v-if="(groupInfo.hostId === this.$store.state.userId) && !groupInfo.hasMeeting" @click='changeHasMeeting' dark color="red">회의 시작</v-btn>
          <v-btn v-else-if="(groupInfo.hostId !== this.$store.state.userId) && groupInfo.hasMeeting" dark color="blue darken-2">회의 참여</v-btn>
          <v-btn v-else dark color="green darken-1">지금은 회의중이 아닙니다</v-btn>
        </v-col>
      </v-row>
      <br>
        <v-divider style="margin-bottom: 8px; margin-left: 10px;"></v-divider>
      <v-row style="width: 100%; height: 15%; margin-left: 20px;">
        <div style="font-size: 24px;">{{ groupInfo.groupIntro }}</div>
      </v-row>
      <v-row style="width: 100%; height: 60%; margin-left: 20px;">
        <v-col cols="6">
          <v-card class="mx-auto" outlined max-width="400" style="padding: 0px;">
            <h2>Member</h2>
            <v-spacer></v-spacer>
            <v-divider></v-divider> 
            <div v-if="members.length===0">그룹원이 없으요<br>초대좀ㅠㅠㅠ</div>
            <v-card-text v-for="(memberInfo, i) in members.slice(0,3)" :key=i style="padding: 5px;">
              <memberCard :userInfo = memberInfo />
            </v-card-text>
            <v-card-actions>
              <v-spacer></v-spacer>
              <GroupMembers :membersInfo=members :groupNo=groupInfo.groupNo :hostId=groupInfo.hostId />
            </v-card-actions>
          </v-card>
        </v-col>
        <v-col cols='6'>
          <div v-if="groupInfo.hostId === this.$store.state.userId">
            <InviteMember :groupNo = groupInfo.groupNo />
            <v-btn @click='destroyGroup'>그룹 파괴</v-btn>
          </div>
        <v-btn color="danger" @click="exitGroup">
          그룹 탈퇴
        </v-btn>
        </v-col>
      </v-row>
    </v-col>

    <!-- 우측 캘린더 부분 -->
    <v-col cols="5" style="width: 100%;">
      <GroupCalendar />
    </v-col>
  </v-row>
</template>

<script>
import axios from 'axios';
import memberCard from './memberCard.vue';
import GroupMembers from './GroupMembers.vue';
import InviteMember from './InviteMember.vue';

import GroupCalendar from './GroupCalendar.vue';
const SERVER_URL = 'http://localhost:8080/videoconference/api/';

export default {
  name: 'group',
  components: {
    memberCard,
    GroupMembers,
    InviteMember,
    GroupCalendar
  },
  props: {
    groupInfo: Object,
  },
  data: () => ({
    members : [],
    type: 'month',
    types: ['month', 'week', 'day', '4day'],
    mode: 'stack',
    modes: ['stack', 'column'],
    weekday: [0, 1, 2, 3, 4, 5, 6],
    weekdays: [
      { text: 'Sun - Sat', value: [0, 1, 2, 3, 4, 5, 6] },
      { text: 'Mon - Sun', value: [1, 2, 3, 4, 5, 6, 0] },
      { text: 'Mon - Fri', value: [1, 2, 3, 4, 5] },
      { text: 'Mon, Wed, Fri', value: [1, 3, 5] },
    ],
    value: '',
    events: [],
    colors: ['blue', 'indigo', 'deep-purple', 'cyan', 'green', 'orange', 'grey darken-1'],
    names: ['Meeting', 'Holiday', 'PTO', 'Travel', 'Event', 'Birthday', 'Conference', 'Party'],
  }),
  methods: {
    getEvents ({ start, end }) {
      const events = [];

      const min = new Date(`${start.date}T00:00:00`);
      const max = new Date(`${end.date}T23:59:59`);
      const days = (max.getTime() - min.getTime()) / 86400000;
      const eventCount = this.rnd(days, days + 20);

      for (let i = 0; i < eventCount; i++) {
        const allDay = this.rnd(0, 3) === 0;
        const firstTimestamp = this.rnd(min.getTime(), max.getTime());
        const first = new Date(firstTimestamp - (firstTimestamp % 900000));
        const secondTimestamp = this.rnd(2, allDay ? 288 : 8) * 900000;
        const second = new Date(first.getTime() + secondTimestamp);

        events.push({
          name: this.names[this.rnd(0, this.names.length - 1)],
          start: first,
          end: second,
          color: this.colors[this.rnd(0, this.colors.length - 1)],
          timed: !allDay,
        });
      }

      this.events = events;
    },
    getEventColor (event) {
      return event.color;
    },
    rnd (a, b) {
      return Math.floor((b - a + 1) * Math.random()) + a;
    },

    exitGroup(){
      axios.delete(SERVER_URL+'groupmember/delno/'+this.groupInfo.groupNo+'/'+this.$store.state.userId)
        .then(() => {
          this.$router.push('/Home');
        })
        .catch(err => console.log(err.response));
    },
    destroyGroup(){
      axios.delete(SERVER_URL+'group/delno/'+this.groupInfo.groupNo)
        .then(() => {
          this.$router.push('/Home');
        })
        .catch(err => console.log(err.response));
    },
    changeHasMeeting(){
      axios.put(SERVER_URL+'group/hasmeeting/'+this.groupInfo.groupNo)
        .then(() => {
          console.log('Changed HasMeeting!');
        });
    }
  },

  mounted() {
    console.log('hi');
    axios.get(SERVER_URL+'groupmember/getno/'+this.groupInfo.groupNo)
      .then(res => {
        this.members = res.data.groupMembers;
      })
      .catch(err => console.log(err.response));
  },
  watch:{
    groupInfo(){
      axios.get(SERVER_URL+'groupmember/getno/'+this.groupInfo.groupNo)
        .then(res => {
          this.members = res.data.groupMembers;
        })
        .catch(err => console.log(err.response));    }
  }
};
</script>

<style>
  .showr{
    font-size: 40px;
  }
  .long{
    font-size: 30px;
  }
</style>
