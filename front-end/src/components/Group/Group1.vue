<template>
  <v-row style="height: 100%; width: 100%;">
    <!-- 좌측 그룹 정보 부분 -->
    <v-col cols="7" style="width: 100%;">
      <v-row style="height: 15%; width: 100%; margin-left: 20px; margin-top: 20px;">
        <v-col cols="8">
          <h1>Team DNS</h1>
          <v-row>
            <v-col cols="2" class="p-0 ml-3">
              <v-img :src="require('../../assets/profile/profile1.jpg')" class="rounded-circle p-0" height="60" width="60"></v-img>
            </v-col>
            <v-row class="ml-0" justify="start" style="height: 100%;">
              <div>
                <span style="font-size: 20px;">정영진</span>
                <br>
                <span style="font-size: 16px;">wjddudwls13@gmail.com</span>
              </div>
            </v-row>
          </v-row>
        </v-col>
        <v-col cols="4">
          <v-btn dark color="red">회의 시작</v-btn>
        </v-col>
      </v-row>
      <br>
        <v-divider style="margin-bottom: 8px;"></v-divider>
      <v-row style="width: 100%; height: 15%; margin-left: 20px;">
        <div style="font-size: 24px;">삼성 청년 소프트웨어 아카데미 3기 대전 1반 2조 Team DNS</div>
      </v-row>
      <v-row style="width: 100%; height: 60%; margin-left: 20px;">
        여긴 뭐넣징
      </v-row>
    </v-col>

    <!-- 우측 캘린더 부분 -->
    <v-col cols="5" style="width: 100%;">
      <div style="margin-top: 30px;">
        <v-sheet tile height="54" color="grey lighten-3" class="d-flex">
          <v-btn icon class="ma-2" @click="$refs.calendar.prev()">
            <v-icon>mdi-chevron-left</v-icon>
          </v-btn>
          <v-spacer></v-spacer>
          <v-row justify="center" align="center">Calendar</v-row>
          <v-spacer></v-spacer>
          <v-btn icon class="ma-2" @click="$refs.calendar.next()">
            <v-icon>mdi-chevron-right</v-icon>
          </v-btn>
        </v-sheet>
        <v-sheet height="600">
          <v-calendar ref="calendar" v-model="value" :type="type" :events="events" :event-overlap-mode="mode" :event-overlap-threshold="30" :event-color="getEventColor" @change="getEvents"></v-calendar>
        </v-sheet>
      </div>
    </v-col>
  </v-row>
</template>

<script>
export default {
  name: 'info',
  data: () => ({
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
  },
};
</script>

<style>
</style>
