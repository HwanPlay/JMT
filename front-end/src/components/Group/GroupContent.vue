<template>
  <v-row>
        {{ groupInfo }}
    <!-- 좌측 그룹 정보 부분 -->
    <v-col cols="7">
      <v-row justify="center">
        <v-btn v-if="!groupInfo.hasMeeting" dark text color="green darken-1">회의 진행중이 아닙니다</v-btn>
        <v-btn v-if="groupInfo.hasMeeting" dark text color="green darken-1">회의 진행중</v-btn>
      </v-row>

      <v-row>
        <v-col cols="8">
          <h2>{{ groupInfo.groupName }}</h2>
        </v-col>

        <v-col cols="4">
          <v-btn v-if="(groupInfo.hostId === this.$store.state.userId) && !groupInfo.hasMeeting" dark color="green">회의 시작</v-btn>
          <v-btn v-if="(groupInfo.hostId !== this.$store.state.userId) && groupInfo.hasMeeting" dark color="blue darken-2">회의 참여</v-btn>
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
              <v-col cols="4">
                <h3>Member</h3>
              </v-col>
              <v-col cols="8">
                <div v-if="groupInfo.hostId === this.$store.state.userId">
                  <InviteMember :groupNo = groupInfo.groupNo />
                </div>
              </v-col>
            </v-row>
            
          <v-card outlined>
            <v-col>
              <div v-if="members.length === 0">그룹원이 없습니다</div>
              <v-card-text v-for="(memberInfo, i) in members.slice(0,3)" :key=i style="padding: 5px;">
                <memberCard :userInfo = memberInfo />
              </v-card-text>
              
              <v-card-actions>
                <v-spacer></v-spacer>
                <GroupMembers :membersInfo=members :groupNo=groupInfo.groupNo :hostId=groupInfo.hostId />
              </v-card-actions>
            </v-col>
            
          </v-card>
        </v-col>
      </v-row>

      <v-col>
        <v-row justify="end">
          <div v-if="groupInfo.hostId === this.$store.state.userId">
            <v-btn @click='destroyGroup'>그룹 파괴</v-btn>
          </div>
          <v-btn dark color="red" @click="exitGroup">
            그룹 탈퇴
          </v-btn>
        </v-row>
      </v-col>
      
    </v-col>

    <!-- 우측 캘린더 부분 -->
    <v-col cols="5">
      <div>
        <v-sheet tile height="54" color="grey lighten-3" class="d-flex">


          <v-row justify="center" align="center">
            <v-btn icon class="ma-2" @click="$refs.calendar.prev()">
              <v-icon>mdi-chevron-left</v-icon>
            </v-btn>
            <!-- <v-spacer></v-spacer> -->
            'Calendar' x => 'M 월' o (안되나?)
            <!-- <v-spacer></v-spacer> -->
            <v-btn icon class="ma-2" @click="$refs.calendar.next()">
              <v-icon>mdi-chevron-right</v-icon>
            </v-btn>
          </v-row>
          

        </v-sheet>
        <v-sheet height="600">
          <v-calendar ref="calendar" v-model="value" :type="type" :events="events" :event-overlap-mode="mode" :event-overlap-threshold="30" :event-color="getEventColor" @change="getEvents"></v-calendar>
        </v-sheet>
      </div>
    </v-col>
  </v-row>
</template>

<script>
import axios from 'axios';
import memberCard from './memberCard.vue';
import GroupMembers from './GroupMembers.vue';
import InviteMember from './InviteMember.vue';

const SERVER_URL = 'http://localhost:8080/videoconference/api/';

export default {
  name: 'group',
  components: {
    memberCard,
    GroupMembers,
    InviteMember,
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

    destroyGroup(){
      axios.delete(SERVER_URL+'group/delno/'+this.groupInfo.groupNo)
        .then(() => {
          this.$router.push('/Home');
        })
        .catch(err => console.log(err.response));
    },

    exitGroup(){
      axios.delete(SERVER_URL+'groupmember/delno/'+this.groupInfo.groupNo+'/'+this.$store.state.userId)
        .then(res => {
          this.$router.push('/Home');
        })
        .catch(err => console.log(err.response));
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
